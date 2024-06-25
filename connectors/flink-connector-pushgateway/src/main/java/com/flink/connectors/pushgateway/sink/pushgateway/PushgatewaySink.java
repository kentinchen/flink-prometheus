package com.flink.connectors.pushgateway.sink.pushgateway;

import com.flink.connectors.pushgateway.table.SchemaLifecycleAwareElementConverter;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.connector.base.sink.AsyncSinkBase;
import org.apache.flink.connector.base.sink.writer.BufferedRequestState;
import org.apache.flink.connector.base.sink.writer.ElementConverter;
import org.apache.flink.core.io.SimpleVersionedSerializer;
import org.apache.flink.util.Preconditions;
import org.apache.flink.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Properties;

@Slf4j
public class PushgatewaySink<InputT> extends AsyncSinkBase<InputT, ArrayList<PushgatewayGaugeEntity>> {
    private final Properties properties;
    private final String pushgateway;
    private final String method;
    private final int batchSize;
    private final boolean debug;
    private final ElementConverter<InputT, ArrayList<PushgatewayGaugeEntity>> elementConverter;

    public PushgatewaySink(int maxBatchSize,
                           int maxInFlightRequests,
                           int maxBufferedRequests,
                           long maxBatchSizeInBytes,
                           long maxTimeInBufferMS,
                           long maxRecordSizeInBytes,
                           String pushgateway, String method, int batchSize, boolean debug,
                           Properties properties,
                           ElementConverter<InputT, ArrayList<PushgatewayGaugeEntity>> elementConverter) {
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
        this.method = method;
        this.batchSize = batchSize;
        this.debug = debug;
        this.properties = properties;
        this.elementConverter = elementConverter;
    }

    public static <InputT> PushgatewaySinkBuilder<InputT> builder() {
        return new PushgatewaySinkBuilder<>();
    }

    @Override
    public StatefulSinkWriter<InputT, BufferedRequestState<ArrayList<PushgatewayGaugeEntity>>> createWriter(InitContext initContext) {
        ElementConverter<InputT, ArrayList<PushgatewayGaugeEntity>> elementConverter = getElementConverter();
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
                pushgateway, method, batchSize, debug,
                Collections.emptyList(),
                properties
        );
    }

    @Override
    public SimpleVersionedSerializer<BufferedRequestState<ArrayList<PushgatewayGaugeEntity>>> getWriterStateSerializer() {
        return new PushgatewaySinkWriterStateSerializer();
    }

    @Override
    public StatefulSinkWriter<InputT, BufferedRequestState<ArrayList<PushgatewayGaugeEntity>>> restoreWriter(
            InitContext context, Collection<BufferedRequestState<ArrayList<PushgatewayGaugeEntity>>> recoveredState) {
        return new PushgatewaySinkWriter<>(
                elementConverter,
                context,
                getMaxBatchSize(),
                getMaxInFlightRequests(),
                getMaxBufferedRequests(),
                getMaxBatchSizeInBytes(),
                getMaxTimeInBufferMS(),
                getMaxRecordSizeInBytes(),
                pushgateway, method, batchSize, debug,
                recoveredState,
                properties
        );
    }
}
