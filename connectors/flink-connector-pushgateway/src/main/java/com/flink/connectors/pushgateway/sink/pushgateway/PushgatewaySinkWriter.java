package com.flink.connectors.pushgateway.sink.pushgateway;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Gauge;
import io.prometheus.client.exporter.PushGateway;
import org.apache.flink.api.common.eventtime.Watermark;
import org.apache.flink.api.connector.sink2.Sink;
import org.apache.flink.connector.base.sink.writer.AsyncSinkWriter;
import org.apache.flink.connector.base.sink.writer.BufferedRequestState;
import org.apache.flink.connector.base.sink.writer.ElementConverter;
import org.apache.flink.metrics.Counter;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Consumer;

public class PushgatewaySinkWriter<InputT> extends AsyncSinkWriter<InputT, PushgatewayGaugeEntity> {
    private final Counter numRecordsSendCounter;
    private final Counter numRecordsSendErrorsCounter;
    private final transient CollectorRegistry pushRegistry = new CollectorRegistry();
    private final transient Map<String, Gauge> gaugeMap = new HashMap<>();
    private transient PushGateway pg;
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
        this.pg = new PushGateway(pushgateway);
        var metrics = context.metricGroup();
        this.numRecordsSendErrorsCounter = metrics.getNumRecordsSendErrorsCounter();
        this.numRecordsSendCounter = metrics.getNumRecordsSendCounter();
    }

    @Override
    protected void submitRequestEntries(List<PushgatewayGaugeEntity> list, Consumer<List<PushgatewayGaugeEntity>> consumer) {
        for (PushgatewayGaugeEntity pushgatewayGaugeEntity : list) {
            if (pushgatewayGaugeEntity != null) {
                pushGauge(pushgatewayGaugeEntity);
            }
        }
    }

    private void pushGauge(PushgatewayGaugeEntity gaugeSinkEntity) {
        //System.err.println("write pushgateway: " + pushgateway);
        numRecordsSendCounter.inc();
        Gauge gauge = gaugeMap.get(gaugeSinkEntity.metricName);
        if (gauge == null) {
            gauge = Gauge.build().name(gaugeSinkEntity.metricName).help(gaugeSinkEntity.metricHelp).register(pushRegistry);
            gaugeMap.put(gaugeSinkEntity.metricName, gauge);
        }
        gauge.set(gaugeSinkEntity.metricValue);
        try {
            pg.push(gauge, gaugeSinkEntity.jobName, gaugeSinkEntity.groupingKey);
        } catch (Exception e) {
            numRecordsSendErrorsCounter.inc();
            Field[] fields = pg.getClass().getDeclaredFields();
            for (Field field : fields) {
                if (field.getName().equals("gatewayBaseURL")) {
                    //设置允许通过反射访问私有变量
                    field.setAccessible(true);
                    try {
                        String gatewayBaseURL = (String) field.get(pg);
                        System.err.println("write pushgateway: " + gatewayBaseURL);
                    } catch (IllegalAccessException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
            e.printStackTrace();
        }
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
