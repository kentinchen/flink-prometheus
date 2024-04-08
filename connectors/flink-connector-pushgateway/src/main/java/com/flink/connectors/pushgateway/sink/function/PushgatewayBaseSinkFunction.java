package com.flink.connectors.pushgateway.sink.function;

import com.flink.connectors.pushgateway.sink.pushgateway.PushgatewayGaugeEntity;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Gauge;
import io.prometheus.client.exporter.PushGateway;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
public abstract class PushgatewayBaseSinkFunction<IN> extends RichSinkFunction<IN> implements ExceptionHolder {
    private final String pushgateway;
    private CollectorRegistry pushRegistry;
    private PushGateway pg;
    private AtomicReference<Exception> exceptionRef = new AtomicReference<>(null);
    private Map<String, Gauge> gaugeMap = new HashMap<>();
    ;

    public PushgatewayBaseSinkFunction(String pushgateway) {
        this.pushgateway = pushgateway;
        this.pg = new PushGateway(this.pushgateway);
        this.pushRegistry = new CollectorRegistry();
    }

    @Override
    public Exception getExceptionRef() {
        return this.exceptionRef.get();
    }

    @Override
    public void setExceptionRef(Exception e) {
        this.exceptionRef.set(e);
    }

    public void pushGauge(PushgatewayGaugeEntity gaugeSinkEntity) {
        //System.err.println("write pushgateway: " + pushgateway);
        log.info(gaugeSinkEntity.toString());
        Gauge gauge = gaugeMap.get(gaugeSinkEntity.metricName);
        if (gauge == null) {
            gauge = Gauge.build().name(gaugeSinkEntity.metricName).help(gaugeSinkEntity.metricHelp).register(pushRegistry);
            gaugeMap.put(gaugeSinkEntity.metricName, gauge);
        }
        gauge.set(gaugeSinkEntity.metricValue);
        try {
            pg.push(gauge, gaugeSinkEntity.jobName, gaugeSinkEntity.groupingKey);
        } catch (Exception e) {
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
            exceptionRef.set(e);
            e.printStackTrace();
        }
    }

    public void open(Configuration parameters) throws Exception {
        System.err.println("open pushgateway: " + pushgateway);
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
