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
    protected Map<Integer, String> tagsIdxMap;
    private boolean schemaOpened = false;
    private final SerializationSchema<RowData> serializationSchema;
    private int metricIdx;
    private int valueIdx;

    public SerializationSchemaElementConverter(int metricIdx, int valueIdx, Map<Integer, String> tagsIdxMap, SerializationSchema<RowData> serializationSchema) {
        this.metricIdx = metricIdx;
        this.valueIdx = valueIdx;
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
        String metricName = metricName1.replace(" ", "").replace("-", "_");
        Double metricValue = rowData.getDouble(valueIdx);
        System.out.println("metricName:" + metricName + " metricValue:" + metricValue);
        return new PushgatewayGaugeEntity("jobName", metricName, metricValue, parseTags(rowData));
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
