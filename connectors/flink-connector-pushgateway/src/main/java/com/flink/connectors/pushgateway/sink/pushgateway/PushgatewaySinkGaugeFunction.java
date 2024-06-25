package com.flink.connectors.pushgateway.sink.pushgateway;

import com.flink.connectors.pushgateway.sink.function.PushgatewayBaseSinkFunction;

import java.util.List;

public class PushgatewaySinkGaugeFunction extends PushgatewayBaseSinkFunction<List<PushgatewayGaugeEntity>> {
    public PushgatewaySinkGaugeFunction(String pushgateway, String method, int batchSize, boolean debug) {
        super(pushgateway, method, batchSize, debug);
    }

    public void constructPoint(List<PushgatewayGaugeEntity> gaugeSinkEntityList) {
        pushGauge(gaugeSinkEntityList);
    }
}