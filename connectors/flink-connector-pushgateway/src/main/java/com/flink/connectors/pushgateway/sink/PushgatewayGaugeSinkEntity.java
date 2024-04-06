package com.flink.connectors.pushgateway.sink;

import com.flink.connectors.pushgateway.utils.DateUtil;

import java.util.TreeMap;

public class PushgatewayGaugeSinkEntity {
    public String jobName;
    public Long timestamp;
    public String metricName;
    public String metricHelp;
    public Double metricValue;
    public TreeMap<String, String> groupingKey;

    public PushgatewayGaugeSinkEntity(String jobName,
                                      String metricName, Double metricValue,
                                      TreeMap<String, String> groupingKey) {
        this.jobName = jobName;
        this.timestamp = DateUtil.getCurrentTimeStamp();
        this.metricName = metricName;
        this.metricHelp = "Help "+ metricName;
        this.metricValue = metricValue;
        this.groupingKey = groupingKey;
    }

    public PushgatewayGaugeSinkEntity(String jobName, Long timestamp,
                                      String metricName, Double metricValue,
                                      TreeMap<String, String> groupingKey) {
        this(jobName,  metricName, metricValue, groupingKey);
        this.timestamp = timestamp;
    }

    public PushgatewayGaugeSinkEntity(String jobName,Long timestamp,
                                      String metricName, String metricHelp, Double metricValue,
                                      TreeMap<String, String> groupingKey) {
        this(jobName, timestamp, metricName, metricValue, groupingKey);
        this.metricHelp = metricHelp;
    }
}
