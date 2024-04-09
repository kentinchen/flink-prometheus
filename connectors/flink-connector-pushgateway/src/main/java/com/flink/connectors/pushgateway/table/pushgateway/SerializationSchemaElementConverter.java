package com.flink.connectors.pushgateway.table.pushgateway;

import com.flink.connectors.pushgateway.sink.pushgateway.PushgatewayGaugeEntity;
import com.flink.connectors.pushgateway.table.SchemaLifecycleAwareElementConverter;
import org.apache.flink.api.common.serialization.SerializationSchema;
import org.apache.flink.api.connector.sink2.Sink.InitContext;
import org.apache.flink.api.connector.sink2.SinkWriter.Context;
import org.apache.flink.table.data.RowData;
import org.apache.flink.util.FlinkRuntimeException;

import java.util.Map;
import java.util.TreeMap;

public class SerializationSchemaElementConverter
        implements SchemaLifecycleAwareElementConverter<RowData, PushgatewayGaugeEntity> {
    private final SerializationSchema<RowData> serializationSchema;
    protected Map<Integer, String> tagsIdxMap;
    private boolean schemaOpened = false;
    private int metricIdx;
    private int valueIdx;
    private String jobName;
    private String jobType;
    private int jobIdx;
    private int typeIdx;
    private int helpIdx;
    private int tsIdx;

    public SerializationSchemaElementConverter(String jobName, String jobType, int jobIdx,int typeIdx,
                                               int metricIdx, int helpIdx,int valueIdx, int tsIdx,
                                               Map<Integer, String> tagsIdxMap, SerializationSchema<RowData> serializationSchema) {
        this.jobName = jobName;
        this.jobType = jobType;
        this.jobIdx = jobIdx;
        this.typeIdx = typeIdx;
        this.metricIdx = metricIdx;
        this.helpIdx = helpIdx;
        this.valueIdx = valueIdx;
        this.tsIdx = tsIdx;
        this.tagsIdxMap = tagsIdxMap;
        this.serializationSchema = serializationSchema;
    }

    @Override
    public void open(InitContext context) {
        if (!schemaOpened) {
            try {
                serializationSchema.open(context.asSerializationSchemaInitializationContext());
                schemaOpened = true;
            } catch (Exception e) {
                throw new FlinkRuntimeException("Failed to initialize serialization schema.", e);
            }
        }
    }

    @Override
    public PushgatewayGaugeEntity apply(RowData rowData, Context context) {
        String metricName1 = rowData.getString(metricIdx).toString();
        String metricName = metricName1.replace(" ", "")
                .replace("-", "_")
                .replace(".", "_");
        Double metricValue = rowData.getDouble(valueIdx);
        System.out.println("metricName:" + metricName + " metricValue:" + metricValue);
        PushgatewayGaugeEntity e = new PushgatewayGaugeEntity(getJobName(rowData), metricName, metricValue, parseTags(rowData));
        setTs(e, rowData);
        setHelp(e, rowData);
        return  e;
    }

    private void setTs(PushgatewayGaugeEntity e, RowData rowData){
        if(tsIdx > 0){
            e.setTimestamp(Long.valueOf(rowData.getString(tsIdx).toString()));
        }
    }

    private void setHelp(PushgatewayGaugeEntity e, RowData rowData){
        if(helpIdx > 0){
            e.setTimestamp(Long.valueOf(rowData.getString(helpIdx).toString()));
        }
    }

    private String getJobName(RowData row){
        if(jobIdx > 0){
            return row.getString(jobIdx).toString();
        }
        return jobName;
    }

    protected TreeMap<String, String> parseTags(RowData row) {
        return tagsIdxMap.isEmpty() ? new TreeMap<>() : doParseTags(tagsIdxMap, row);
    }

    private TreeMap<String, String> doParseTags(Map<Integer, String> tagsIdxMap, RowData row) {
        TreeMap<String, String> tags = new TreeMap<>();
        for (Map.Entry<Integer, String> entry : tagsIdxMap.entrySet()) {
            int idx = entry.getKey();
            String tagKey = entry.getValue();
            Object rawTagValue = row.getString(idx);
            String tagValue = (rawTagValue == null) ? "None" : rawTagValue.toString();
            tags.put(tagKey, tagValue);
            System.out.println("tag idx:" + idx + " tagKey:" + tagKey + " tagValue:" + tagValue);
        }
        return tags;
    }
}
