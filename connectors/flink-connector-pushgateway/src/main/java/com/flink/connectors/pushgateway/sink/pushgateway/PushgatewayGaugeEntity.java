package com.flink.connectors.pushgateway.sink.pushgateway;

import com.flink.connectors.pushgateway.utils.DateUtil;

import java.io.Serializable;
import java.util.TreeMap;

public class PushgatewayGaugeEntity implements Serializable {
    public String jobName;
    public Long timestamp;
    public String metricName;
    public String metricHelp;
    public Double metricValue;
    public TreeMap<String, String> groupingKey;

    public PushgatewayGaugeEntity(String jobName,
                                  String metricName, Double metricValue,
                                  TreeMap<String, String> groupingKey) {
        this.jobName = jobName;
        this.timestamp = DateUtil.getCurrentTimeStamp();
        this.metricName = metricName;
        this.metricHelp = "Help " + metricName;
        this.metricValue = metricValue;
        this.groupingKey = groupingKey;
    }

    public PushgatewayGaugeEntity(String jobName, Long timestamp,
                                  String metricName, Double metricValue,
                                  TreeMap<String, String> groupingKey) {
        this(jobName, metricName, metricValue, groupingKey);
        this.timestamp = timestamp;
    }

    public PushgatewayGaugeEntity(String jobName, Long timestamp,
                                  String metricName, String metricHelp, Double metricValue,
                                  TreeMap<String, String> groupingKey) {
        this(jobName, timestamp, metricName, metricValue, groupingKey);
        this.metricHelp = metricHelp;
    }

    public long getSizeInBytes() {
        return 40;      //TODO 计算计算大小
    }
}
