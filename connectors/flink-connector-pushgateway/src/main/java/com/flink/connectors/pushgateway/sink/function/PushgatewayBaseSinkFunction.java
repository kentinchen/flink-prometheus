package com.flink.connectors.pushgateway.sink.function;

import com.flink.connectors.pushgateway.sink.pushgateway.PushgatewayGaugeEntity;
import com.flink.connectors.pushgateway.utils.DateUtil;
import com.flink.connectors.pushgateway.utils.ExcpUtil;
import com.flink.connectors.pushgateway.utils.HttpUtil;
import com.flink.connectors.pushgateway.utils.ReflectionUtils;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Gauge;
import io.prometheus.client.exporter.PushGateway;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static com.flink.connectors.pushgateway.config.ConnectorConfigConstants.CONST_DIRECT;
import static com.flink.connectors.pushgateway.config.ConnectorConfigConstants.CONST_PRODUCT;

@Slf4j
public abstract class PushgatewayBaseSinkFunction<IN> extends RichSinkFunction<IN> implements ExceptionHolder {
    private final String pushgateway;
    private final PushGateway pg;
    private final String method;
    private final boolean debug;
    private final int batchSize;

    private final AtomicReference<Exception> exceptionRef = new AtomicReference<>(null);
    private final CollectorRegistry registry = new CollectorRegistry();
    private final Map<String, Gauge> gaugeMap = new HashMap<>();
    private final Map<String, HashMap<PushgatewayGaugeEntity, String>> metricMap = new HashMap<>();
    public TreeMap<String, String> groupingKey = new TreeMap<>();
    private int count = 0;
    private int ttl = 0;

    public PushgatewayBaseSinkFunction(String pushgateway, String method, int batchSize, boolean debug) {
        this.method = method;
        this.debug = debug;
        this.batchSize = batchSize;
        this.pushgateway = pushgateway;
        pg = new PushGateway(this.pushgateway);
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
        List<PushgatewayGaugeEntity> gaugeSinkEntityList = new ArrayList<>();
        gaugeSinkEntityList.add(gaugeSinkEntity);
        this.pushGauge(gaugeSinkEntityList);
    }

    private TreeMap<String, String> getGroupingKey(PushgatewayGaugeEntity gaugeSinkEntity) {
        if (groupingKey.isEmpty()) {
            for (Map.Entry<String, String> entry : gaugeSinkEntity.getLabelKey().entrySet()) {
                if (entry.getKey().equalsIgnoreCase(CONST_PRODUCT)) {
                    groupingKey.put(CONST_PRODUCT, entry.getValue());
                }
            }
            if (groupingKey.isEmpty()) {
                groupingKey.put(CONST_PRODUCT, "");
            }
        }
        return groupingKey;
    }

    private String getProductKey(PushgatewayGaugeEntity gaugeSinkEntity) {
        TreeMap<String, String> groupingKey = getGroupingKey(gaugeSinkEntity);
        return groupingKey.get(CONST_PRODUCT);
    }

    public void pushGauge(List<PushgatewayGaugeEntity> gaugeSinkEntityList) {
        if (CONST_DIRECT.equalsIgnoreCase(method)) {
            pushGaugeDirect(gaugeSinkEntityList);
        } else {
            pushGaugeRegistry(gaugeSinkEntityList);
        }
    }

    // 通过post批量推送
    private void pushGaugeDirect(List<PushgatewayGaugeEntity> gaugeSinkEntityList) {
        for (PushgatewayGaugeEntity gaugeSinkEntity : gaugeSinkEntityList) {
            if (debug) {
                System.out.println(gaugeSinkEntity.toString());
            }
            HashMap<PushgatewayGaugeEntity, String> map = metricMap.computeIfAbsent(gaugeSinkEntity.getMetricName(), k -> new HashMap<>());
            map.put(gaugeSinkEntity, "");
            metricMap.put(gaugeSinkEntity.getMetricName(), map);
            count++;
        }
        if (count > batchSize) {
            pushMetrics();
            count = 0;
        }
    }

    private void pushMetrics() {
        for (Map.Entry<String, HashMap<PushgatewayGaugeEntity, String>> entry : metricMap.entrySet()) {
            HashMap<PushgatewayGaugeEntity, String> map1 = pushMetrics(entry.getValue());
            metricMap.put(entry.getKey(), map1);
        }
    }

    private HashMap<PushgatewayGaugeEntity, String> pushMetrics(HashMap<PushgatewayGaugeEntity, String> map) {
        ArrayList<PushgatewayGaugeEntity> gaugeSinkEntityList = new ArrayList<>();
        log.info("开始curl推送指标");
        StringBuilder sb = new StringBuilder();
        PushgatewayGaugeEntity[] array = map.keySet().toArray(new PushgatewayGaugeEntity[0]);
        PushgatewayGaugeEntity e = array[0];
        sb.append(e.getMetricHead());
        for (PushgatewayGaugeEntity gaugeEntity : array) {
            sb.append(gaugeEntity.getMetric());
            //ttl分钟有更新就保留
            if (gaugeEntity.timestamp > DateUtil.getCurrentTimeStamp() - 60L * ttl) {
                gaugeSinkEntityList.add(gaugeEntity);
            }
        }
        HttpUtil util = new HttpUtil();
        util.postForHttpClient("http://" + this.pushgateway, "/metrics/job/flink/product/" + getProductKey(e), sb.toString());
        //清理超过ttl的指标
        map.clear();
        if (ttl > 0) {
            for (PushgatewayGaugeEntity gaugeEntity : gaugeSinkEntityList) {
                map.put(gaugeEntity, "");
            }
        }
        log.info("完成curl推送指标名:{},指标数{}", e.metricName, array.length);
        return map;
    }

    // 通过registry推送
    private void pushGaugeRegistry(List<PushgatewayGaugeEntity> gaugeSinkEntityList) {
        try {
            for (PushgatewayGaugeEntity gaugeSinkEntity : gaugeSinkEntityList) {
                if (debug) {
                    System.out.println(gaugeSinkEntity.toString());
                }
                Gauge gauge = gaugeMap.get(gaugeSinkEntity.getMetricName());
                try {
                    if (gauge == null) {
                        gauge = Gauge.build(gaugeSinkEntity.getMetricName(), gaugeSinkEntity.getMetricHelp()).
                                labelNames(gaugeSinkEntity.getLabelKey().keySet().toArray(new String[0])).create();
                        gauge.labels(gaugeSinkEntity.getLabelKey().values().toArray(new String[0])).set(gaugeSinkEntity.metricValue);
                        gauge.register(registry);
                        gaugeMap.put(gaugeSinkEntity.getMetricName(), gauge);
                    } else {
                        gauge.labels(gaugeSinkEntity.getLabelKey().values().toArray(new String[0])).set(gaugeSinkEntity.metricValue);
                    }
                } catch (IllegalArgumentException e) {
                    log.error("gaugeSinkEntity:{}", gaugeSinkEntity);
                    log.error("gaugeLabels:{}", gaugeSinkEntity.getLabelKey().keySet());
                    log.error("gaugeLValues:{}", gaugeSinkEntity.getLabelKey().values());
                }
                count++;
            }

            if (count > batchSize) {
                log.info("开始推送指标");
                PushgatewayGaugeEntity gaugeSinkEntity = gaugeSinkEntityList.get(0);
                pg.pushAdd(registry, gaugeSinkEntity.jobName, getGroupingKey(gaugeSinkEntity));
                pushFlinkMetric();
                log.info("完成推送指标{}", count);
                count = 0;
            }
        } catch (Exception e) {
            exceptionRef.set(e);
            log.error(ExcpUtil.getStackTraceString(e));
        }
    }

    private void printGauge(Gauge gauge) {
        try {
            List<String> fieldValue = (List<String>) ReflectionUtils.getFieldValue(gauge, "labelNames");
            String[] labelArray = fieldValue.toArray(new String[0]);
            log.error("gauge Labels size:{}", fieldValue.size());
            log.error("gauge Labels name:{}", Arrays.toString(labelArray));
        } catch (Exception e) {
            log.error("gauge labels field failed", e);
        }
    }

    private void pushFlinkMetric() throws IOException {
        CollectorRegistry registry = CollectorRegistry.defaultRegistry;
        TreeMap<String, String> groupingKey = new TreeMap<>();
        groupingKey.put(CONST_PRODUCT, "flink");
        pg.pushAdd(registry, "flink_batch_job", groupingKey);
    }

    public void open(Configuration parameters) {
        log.info("open pushgateway: " + pushgateway);
    }

    public void invoke(IN value, SinkFunction.Context context) {
        constructPoint(value);
    }

    public void close() {
    }

    public abstract void constructPoint(IN paramIN);
}
