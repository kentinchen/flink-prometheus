package com.flink.connectors.pushgateway.sink.pushgateway;

import com.flink.connectors.pushgateway.table.SchemaLifecycleAwareElementConverter;
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

public class PushgatewaySink<InputT> extends AsyncSinkBase<InputT, PushgatewayGaugeEntity> {
    private final Properties properties;
    private final String pushgateway;
    private final ElementConverter<InputT, PushgatewayGaugeEntity> elementConverter;

    public PushgatewaySink(int maxBatchSize,
                           int maxInFlightRequests,
                           int maxBufferedRequests,
                           long maxBatchSizeInBytes,
                           long maxTimeInBufferMS,
                           long maxRecordSizeInBytes,
                           String pushgateway, Properties properties, ElementConverter<InputT, PushgatewayGaugeEntity> elementConverter) {
        super(elementConverter,
                maxBatchSize,
                maxInFlightRequests,
                maxBufferedRequests,
                maxBatchSizeInBytes,
                maxTimeInBufferMS,
                maxRecordSizeInBytes
        );
        Preconditions.checkArgument(!StringUtils.isNullOrWhitespaceOnly(pushgateway), "The endpoint URL must be set when initializing pushgateway Sink.");
        this.pushgateway = pushgateway;
        this.properties = properties;
        this.elementConverter = elementConverter;
    }

    public static <InputT> PushgatewaySinkBuilder<InputT> builder() {
        return new PushgatewaySinkBuilder<>();
    }

    @Override
    public StatefulSinkWriter<InputT, BufferedRequestState<PushgatewayGaugeEntity>> createWriter(InitContext initContext) throws IOException {
        ElementConverter<InputT, PushgatewayGaugeEntity> elementConverter = getElementConverter();
        if (elementConverter instanceof SchemaLifecycleAwareElementConverter) {
            // This cast is needed for Flink 1.15.3 build
            ((SchemaLifecycleAwareElementConverter<?, ?>) elementConverter).open(initContext);
        }
        return new PushgatewaySinkWriter<>(
                elementConverter,
                initContext,
                getMaxBatchSize(),
                getMaxInFlightRequests(),
                getMaxBufferedRequests(),
                getMaxBatchSizeInBytes(),
                getMaxTimeInBufferMS(),
                getMaxRecordSizeInBytes(),
                pushgateway,
                Collections.emptyList(),
                properties
        );
    }

    @Override
    public SimpleVersionedSerializer<BufferedRequestState<PushgatewayGaugeEntity>> getWriterStateSerializer() {
        return new PushgatewaySinkWriterStateSerializer();
    }

    @Override
    public StatefulSinkWriter<InputT, BufferedRequestState<PushgatewayGaugeEntity>> restoreWriter(
            InitContext context, Collection<BufferedRequestState<PushgatewayGaugeEntity>> recoveredState) throws IOException {
        return new PushgatewaySinkWriter<>(
                elementConverter,
                context,
                getMaxBatchSize(),
                getMaxInFlightRequests(),
                getMaxBufferedRequests(),
                getMaxBatchSizeInBytes(),
                getMaxTimeInBufferMS(),
                getMaxRecordSizeInBytes(),
                pushgateway,
                recoveredState,
                properties
        );
    }
}
