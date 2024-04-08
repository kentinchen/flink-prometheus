package com.flink.connectors.pushgateway.sink.pushgateway;

import com.flink.connectors.pushgateway.sink.function.PushgatewayBaseSinkFunction;

public class PushgatewaySinkGaugeFunction extends PushgatewayBaseSinkFunction<PushgatewayGaugeEntity> {
    public PushgatewaySinkGaugeFunction(String pushgateway) {
        super(pushgateway);
    }

    public void constructPoint(PushgatewayGaugeEntity gaugeSinkEntity) {
        pushGauge(gaugeSinkEntity);
    }
}