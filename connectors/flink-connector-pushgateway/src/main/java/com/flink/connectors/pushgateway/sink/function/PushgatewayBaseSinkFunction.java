package com.flink.connectors.pushgateway.sink.function;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Gauge;
import io.prometheus.client.exporter.PushGateway;
import lombok.Getter;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicReference;

public abstract class PushgatewayBaseSinkFunction<IN> extends RichSinkFunction<IN> implements ExceptionHolder {
    private final String pushgateway;
    private transient AtomicReference<Exception> exceptionRef;
    PushGateway pg;
    private CollectorRegistry pushRegistry;

    @Getter
    //private transient HashMap<String, Gauge> gaugeMap= new HashMap<>();
    private transient HashMap<String, Double> gaugeValue= new HashMap<>();

    public PushgatewayBaseSinkFunction(String pushgateway) {
        this.pushgateway = pushgateway;
    }

    @Override
    public Exception getExceptionRef() {
        return this.exceptionRef.get();
    }

    @Override
    public void setExceptionRef(Exception e) {
        this.exceptionRef.set(e);
    }

    public void pushGauge(String jobName, String metricName, Double metricValue, TreeMap<String, String> groupingKey) throws IOException {
        CollectorRegistry pushRegistry = new CollectorRegistry();
        Gauge temperature = Gauge.build()
                .name(metricName)
                .help(metricName)
                .labelNames("location")
                //.unit(Unit.CELSIUS)
                .register(pushRegistry);
        temperature.set(metricValue);
        pg.push(pushRegistry, jobName, groupingKey);
    }
    public void open(Configuration parameters) throws Exception {
        pg = new PushGateway(this.pushgateway);
    }

    public void invoke(IN value, SinkFunction.Context context) throws Exception {
        constructPoint(value);
    }

    public void close() throws Exception {
        synchronized (PushgatewayBaseSinkFunction.class) {

        }
    }

    public abstract void constructPoint(IN paramIN);
}
