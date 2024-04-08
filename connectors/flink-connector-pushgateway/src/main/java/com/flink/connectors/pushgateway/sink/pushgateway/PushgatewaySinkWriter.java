package com.flink.connectors.pushgateway.sink.pushgateway;

import org.apache.flink.api.common.eventtime.Watermark;
import org.apache.flink.api.connector.sink2.Sink;
import org.apache.flink.connector.base.sink.writer.AsyncSinkWriter;
import org.apache.flink.connector.base.sink.writer.BufferedRequestState;
import org.apache.flink.connector.base.sink.writer.ElementConverter;
import org.apache.flink.metrics.Counter;
import org.apache.flink.metrics.groups.SinkWriterMetricGroup;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.function.Consumer;

public class PushgatewaySinkWriter<InputT> extends AsyncSinkWriter<InputT, PushgatewayGaugeEntity> {
    private final Counter numRecordsSendCounter;
    private final Counter numRecordsSendErrorsCounter;
    private PushgatewaySinkGaugeFunction pushgatewaySinkGaugeFunction;
    private Properties properties;

    public PushgatewaySinkWriter(
            ElementConverter<InputT, PushgatewayGaugeEntity> elementConverter,
            Sink.InitContext context,
            int maxBatchSize,
            int maxInFlightRequests,
            int maxBufferedRequests,
            long maxBatchSizeInBytes,
            long maxTimeInBufferMS,
            long maxRecordSizeInBytes,
            String pushgateway,
            Collection<BufferedRequestState<PushgatewayGaugeEntity>> bufferedRequestStates,
            Properties properties) {
        super(elementConverter, context, maxBatchSize, maxInFlightRequests, maxBufferedRequests,
                maxBatchSizeInBytes, maxTimeInBufferMS, maxRecordSizeInBytes, bufferedRequestStates);
        this.properties = properties;
        SinkWriterMetricGroup metrics = context.metricGroup();
        this.numRecordsSendErrorsCounter = metrics.getNumRecordsSendErrorsCounter();
        this.numRecordsSendCounter = metrics.getNumRecordsSendCounter();
        pushgatewaySinkGaugeFunction = new PushgatewaySinkGaugeFunction(pushgateway);
    }

    @Override
    protected void submitRequestEntries(List<PushgatewayGaugeEntity> list, Consumer<List<PushgatewayGaugeEntity>> consumer) {
        for (PushgatewayGaugeEntity pushgatewayGaugeEntity : list) {
            if (pushgatewayGaugeEntity != null) {
                pushgatewaySinkGaugeFunction.constructPoint(pushgatewayGaugeEntity);
            }
        }
        consumer.accept(Collections.emptyList());
    }

    @Override
    protected long getSizeInBytes(PushgatewayGaugeEntity pushgatewayGaugeEntity) {
        return pushgatewayGaugeEntity.getSizeInBytes();
    }

    @Override
    public void writeWatermark(Watermark watermark) throws IOException, InterruptedException {
        super.writeWatermark(watermark);
    }
}
