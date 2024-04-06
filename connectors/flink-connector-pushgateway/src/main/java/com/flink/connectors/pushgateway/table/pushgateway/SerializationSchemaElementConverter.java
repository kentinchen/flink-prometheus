package com.flink.connectors.pushgateway.table.pushgateway;

import com.flink.connectors.pushgateway.sink.pushgateway.PushgatewayGaugeEntity;
import com.flink.connectors.pushgateway.table.SchemaLifecycleAwareElementConverter;
import org.apache.flink.api.common.serialization.SerializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.connector.sink2.Sink.InitContext;
import org.apache.flink.api.connector.sink2.SinkWriter.Context;
import org.apache.flink.table.data.RowData;
import org.apache.flink.table.types.DataType;
import org.apache.flink.types.Row;
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
    private String[] fieldNames;
    private TypeInformation[] fieldTypes;

    public SerializationSchemaElementConverter(int metricIdx, int valueIdx, Map<Integer, String> tagsIdxMap,
                                               String[] fieldNames, TypeInformation[] fieldTypes, DataType producedDataType,
                                               SerializationSchema<RowData> serializationSchema) {
        this.metricIdx = metricIdx;
        this.valueIdx = valueIdx;
        this.tagsIdxMap = tagsIdxMap;
        this.fieldNames = fieldNames;
        this.fieldTypes = fieldTypes;
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
        TreeMap<String, String> tags = new TreeMap<>();
        tags.put("tagK1", "kentin");
        return new PushgatewayGaugeEntity("job", "ecs_load", 0.23, tags);
        //return new PushgatewayGaugeEntity(jobName, parseMetric(null), parseValue(null), parseTags(null));
    }

    protected String parseMetric(Row row) {
        return row.getField(this.metricIdx).toString();
    }

    protected Double parseValue(Row row) {
        return Double.valueOf((String) row.getField(this.valueIdx));
    }

    protected TreeMap<String, String> parseTags(Row row) {
        return tagsIdxMap.isEmpty() ? new TreeMap<>() : doParseTags(tagsIdxMap, row);
    }

    private TreeMap<String, String> doParseTags(Map<Integer, String> tagsIdxMap, Row row) {
        TreeMap<String, String> tags = new TreeMap<>();
        for (Map.Entry<Integer, String> entry : tagsIdxMap.entrySet()) {
            int idx = entry.getKey();
            String tagKey = entry.getValue();
            Object rawTagValue = row.getField(idx);
            String tagValue = (rawTagValue == null) ? "None" : rawTagValue.toString();
            tags.put(tagKey, tagValue);
        }
        return tags;
    }
}
