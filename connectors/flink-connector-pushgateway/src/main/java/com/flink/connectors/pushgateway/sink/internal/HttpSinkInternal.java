package com.flink.connectors.pushgateway.sink.internal;

import com.flink.connectors.pushgateway.config.ConnectorConfigConstants;
import com.flink.connectors.pushgateway.config.SinkRequestSubmitMode;
import com.flink.connectors.pushgateway.sink.http.HttpSinkRequestEntry;
import com.flink.connectors.pushgateway.sink.http.HttpSinkWriter;
import com.flink.connectors.pushgateway.sink.http.HttpSinkWriterStateSerializer;
import com.flink.connectors.pushgateway.sink.http.RequestSubmitterFactory;
import com.flink.connectors.pushgateway.sink.httpclient.BatchRequestSubmitterFactory;
import com.flink.connectors.pushgateway.sink.httpclient.HttpRequest;
import com.flink.connectors.pushgateway.sink.httpclient.PerRequestRequestSubmitterFactory;
import com.flink.connectors.pushgateway.table.SchemaLifecycleAwareElementConverter;
import com.flink.connectors.pushgateway.table.callback.HttpPostRequestCallback;
import org.apache.flink.connector.base.sink.AsyncSinkBase;
import org.apache.flink.connector.base.sink.writer.BufferedRequestState;
import org.apache.flink.connector.base.sink.writer.ElementConverter;
import org.apache.flink.core.io.SimpleVersionedSerializer;
import org.apache.flink.util.Preconditions;
import org.apache.flink.util.StringUtils;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Properties;

public class HttpSinkInternal<InputT> extends AsyncSinkBase<InputT, HttpSinkRequestEntry> {
    private final String endpointUrl;
    private final HttpPostRequestCallback<HttpRequest> httpPostRequestCallback;
    private final Properties properties;

    protected HttpSinkInternal(
            ElementConverter<InputT, HttpSinkRequestEntry> elementConverter,
            int maxBatchSize,
            int maxInFlightRequests,
            int maxBufferedRequests,
            long maxBatchSizeInBytes,
            long maxTimeInBufferMS,
            long maxRecordSizeInBytes,
            String endpointUrl,
            HttpPostRequestCallback<HttpRequest> httpPostRequestCallback,
            Properties properties) {
        super(
                elementConverter,
                maxBatchSize,
                maxInFlightRequests,
                maxBufferedRequests,
                maxBatchSizeInBytes,
                maxTimeInBufferMS,
                maxRecordSizeInBytes
        );
        Preconditions.checkArgument(!StringUtils.isNullOrWhitespaceOnly(endpointUrl), "The endpoint URL must be set when initializing HTTP Sink.");
        this.endpointUrl = endpointUrl;
        this.httpPostRequestCallback = Preconditions.checkNotNull(httpPostRequestCallback, "Post request callback must be set when initializing HTTP Sink.");
        this.properties = properties;
    }

    @Override
    public StatefulSinkWriter<InputT, BufferedRequestState<HttpSinkRequestEntry>> createWriter(InitContext context) throws IOException {
        ElementConverter<InputT, HttpSinkRequestEntry> elementConverter = getElementConverter();
        if (elementConverter instanceof SchemaLifecycleAwareElementConverter) {
            // This cast is needed for Flink 1.15.3 build
            ((SchemaLifecycleAwareElementConverter<?, ?>) elementConverter).open(context);
        }
        return new HttpSinkWriter<>(
                elementConverter,
                context,
                getMaxBatchSize(),
                getMaxInFlightRequests(),
                getMaxBufferedRequests(),
                getMaxBatchSizeInBytes(),
                getMaxTimeInBufferMS(),
                getMaxRecordSizeInBytes(),
                endpointUrl,
                Collections.emptyList(),
                properties
        );
    }

    @Override
    public StatefulSinkWriter<InputT, BufferedRequestState<HttpSinkRequestEntry>> restoreWriter(
            InitContext context, Collection<BufferedRequestState<HttpSinkRequestEntry>> recoveredState)
            throws IOException {
        return new HttpSinkWriter<>(
                getElementConverter(),
                context,
                getMaxBatchSize(),
                getMaxInFlightRequests(),
                getMaxBufferedRequests(),
                getMaxBatchSizeInBytes(),
                getMaxTimeInBufferMS(),
                getMaxRecordSizeInBytes(),
                endpointUrl,
                recoveredState,
                properties
        );
    }

    @Override
    public SimpleVersionedSerializer<BufferedRequestState<HttpSinkRequestEntry>>
    getWriterStateSerializer() {
        return new HttpSinkWriterStateSerializer();
    }

    private RequestSubmitterFactory getRequestSubmitterFactory() {
        if (SinkRequestSubmitMode.SINGLE.getMode().equalsIgnoreCase(
                properties.getProperty(ConnectorConfigConstants.SINK_HTTP_REQUEST_MODE))) {
            return new PerRequestRequestSubmitterFactory();
        }
        return new BatchRequestSubmitterFactory(getMaxBatchSize());
    }
}
