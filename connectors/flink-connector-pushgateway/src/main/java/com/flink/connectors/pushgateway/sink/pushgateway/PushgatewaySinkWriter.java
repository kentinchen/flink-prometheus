package com.flink.connectors.pushgateway.sink.pushgateway;

import org.apache.flink.api.common.eventtime.Watermark;
import org.apache.flink.api.connector.sink2.Sink;
import org.apache.flink.connector.base.sink.writer.AsyncSinkWriter;
import org.apache.flink.connector.base.sink.writer.BufferedRequestState;
import org.apache.flink.connector.base.sink.writer.ElementConverter;
import org.apache.flink.metrics.groups.SinkWriterMetricGroup;

import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;

public class PushgatewaySinkWriter<InputT> extends AsyncSinkWriter<InputT, ArrayList<PushgatewayGaugeEntity>> {
    private final PushgatewaySinkGaugeFunction pushgatewaySinkGaugeFunction;

    public PushgatewaySinkWriter(
            ElementConverter<InputT, ArrayList<PushgatewayGaugeEntity>> elementConverter,
            Sink.InitContext context,
            int maxBatchSize,
            int maxInFlightRequests,
            int maxBufferedRequests,
            long maxBatchSizeInBytes,
            long maxTimeInBufferMS,
            long maxRecordSizeInBytes,
            String pushgateway, String method, int batchSize, boolean debug,
            Collection<BufferedRequestState<ArrayList<PushgatewayGaugeEntity>>> bufferedRequestStates,
            Properties properties) {
        super(elementConverter, context, maxBatchSize, maxInFlightRequests, maxBufferedRequests,
                maxBatchSizeInBytes, maxTimeInBufferMS, maxRecordSizeInBytes, bufferedRequestStates);
        SinkWriterMetricGroup metrics = context.metricGroup();
        pushgatewaySinkGaugeFunction = new PushgatewaySinkGaugeFunction(pushgateway, method, batchSize, debug);
    }

    @Override
    protected void submitRequestEntries(List<ArrayList<PushgatewayGaugeEntity>> list, Consumer<List<ArrayList<PushgatewayGaugeEntity>>> consumer) {
        for (ArrayList<PushgatewayGaugeEntity> pushgatewayGaugeEntityList : list) {
            if (pushgatewayGaugeEntityList != null) {
                pushgatewaySinkGaugeFunction.constructPoint(pushgatewayGaugeEntityList);
            }
        }
        consumer.accept(Collections.emptyList());
    }

    @Override
    protected long getSizeInBytes(ArrayList<PushgatewayGaugeEntity> pushgatewayGaugeEntity) {
        return pushgatewayGaugeEntity.get(0).getSizeInBytes() * pushgatewayGaugeEntity.size();
    }

    @Override
    public void writeWatermark(Watermark watermark) throws IOException, InterruptedException {
        super.writeWatermark(watermark);
    }
}
