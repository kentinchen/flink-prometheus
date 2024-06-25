package com.flink.connectors.pushgateway.sink.pushgateway;

import com.flink.connectors.pushgateway.utils.DateUtil;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

@Getter
@Setter
public class PushgatewayGaugeEntity implements Serializable {
    public String jobName;
    public Long timestamp;
    public String metricName;
    public String metricHelp;
    public Double metricValue;

    public TreeMap<String, String> labelKey;

    public PushgatewayGaugeEntity(String jobName,
                                  String metricName, Double metricValue,
                                  TreeMap<String, String> labelKey) {
        this.jobName = jobName;
        this.timestamp = DateUtil.getCurrentTimeStamp();
        this.metricName = metricName;
        this.metricHelp = "None";
        this.metricValue = metricValue;
        this.labelKey = labelKey;
    }

    public PushgatewayGaugeEntity(String jobName, Long timestamp,
                                  String metricName, Double metricValue,
                                  TreeMap<String, String> labelKey) {
        this(jobName, metricName, metricValue, labelKey);
        this.timestamp = timestamp;
    }

    public PushgatewayGaugeEntity(String jobName, Long timestamp,
                                  String metricName, String metricHelp, Double metricValue,
                                  TreeMap<String, String> labelKey) {
        this(jobName, timestamp, metricName, metricValue, labelKey);
        this.metricHelp = metricHelp;
    }

    public long getSizeInBytes() {
        return 40;      //TODO 计算计算大小
    }

    @Override
    public String toString() {
        return "jobName:" + jobName +
                " timestamp:" + timestamp +
                " metricName:" + metricName +
                " metricValue:" + metricValue +
                " labelKey:[" + getLabelKey(labelKey) + "]";
    }

    @Override
    public int hashCode() {
        StringBuilder result = new StringBuilder(metricName + " {");
        for (Map.Entry<String, String> entry : labelKey.entrySet()) {
            result.append(entry.getKey()).append("=\"").append(entry.getValue()).append("\"").append(",");
        }
        result.append("} ");
        return result.toString().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof PushgatewayGaugeEntity)) {
            return false;
        }
        PushgatewayGaugeEntity other = (PushgatewayGaugeEntity) obj;
        for (Map.Entry<String, String> entry : labelKey.entrySet()) {
            String key = entry.getKey();
            if(!other.labelKey.containsKey(key)) {
                return false;
            }
            if(!other.labelKey.get(key).equals(labelKey.get(key))) {
                return false;
            }
        }
        return true;
    }

    private String getLabelKey(TreeMap<String, String> labelKey) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : labelKey.entrySet()) {
            sb.append(entry.getKey()).append(":").append(entry.getValue()).append(",");
        }
        return sb.toString();
    }

    public String getMetricHead() {
        return "# TYPE " + metricName + " gauge\n" +
                "# HELP " + metricName + " gauge\n";
    }

    public String getMetric() {
        StringBuilder result = new StringBuilder(metricName + " {");
        for (Map.Entry<String, String> entry : labelKey.entrySet()) {
            result.append(entry.getKey()).append("=\"").append(normalizeLabelValue(entry.getValue())).append("\"").append(",");
        }
        result.append("} ").append(metricValue).append("\n");
        return result.toString().replaceAll(",}","}");
    }

    //规整标签值
    private String normalizeLabelValue(String str){
        return str.replaceAll("\r", "");
                //.replaceAll("{", "\\x7B")
                //.replaceAll("}", "\\x7D");
    }
}
