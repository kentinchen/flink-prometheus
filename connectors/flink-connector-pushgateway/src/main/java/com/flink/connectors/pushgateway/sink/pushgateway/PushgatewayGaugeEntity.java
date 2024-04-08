package com.flink.connectors.pushgateway.sink.pushgateway;

import com.flink.connectors.pushgateway.utils.DateUtil;

import java.io.Serializable;
import java.util.Map;
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
        this.metricHelp = "None";
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

    @Override
    public String toString() {
        return "jobName:" + jobName +
                "timestamp:" + timestamp +
                "metricName:" + metricName +
                "metricValue" + metricValue +
                "groupingKey:[" + getGroupingKey(groupingKey) + "]";
    }

    private String getGroupingKey(TreeMap<String, String> groupingKey) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : groupingKey.entrySet()) {
            sb.append(entry.getKey()).append(":").append(entry.getValue()).append(",");
        }
        return sb.toString();
    }
}
