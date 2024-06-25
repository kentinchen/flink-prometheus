package com.flink.connectors.pushgateway.table.pushgateway;

import com.flink.connectors.pushgateway.sink.pushgateway.PushgatewayGaugeEntity;
import com.flink.connectors.pushgateway.table.SchemaLifecycleAwareElementConverter;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.serialization.SerializationSchema;
import org.apache.flink.api.connector.sink2.Sink.InitContext;
import org.apache.flink.api.connector.sink2.SinkWriter.Context;
import org.apache.flink.table.data.RowData;
import org.apache.flink.util.FlinkRuntimeException;

import java.util.ArrayList;
import java.util.Map;

@Slf4j
public class SerializationSchemaElementColumnConverter extends AbstractElementColumnConverter
        implements SchemaLifecycleAwareElementConverter<RowData, ArrayList<PushgatewayGaugeEntity>> {

    protected final int tsIdx;
    private final String jobName;
    private final SerializationSchema<RowData> serializationSchema;
    private final Map<Integer, String> metricIdxMap;
    private final Map<Integer, String> tagsIdxMap;
    protected int helpIdx;
    private boolean schemaOpened = false;

    public SerializationSchemaElementColumnConverter(String jobName, int tsIdx, Map<Integer, String> metricIdxMap, Map<Integer, String> tagsIdxMap, SerializationSchema<RowData> serializationSchema) {
        this.jobName = jobName;
        this.tsIdx = tsIdx;
        this.metricIdxMap = metricIdxMap;
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
    public ArrayList<PushgatewayGaugeEntity> apply(RowData rowData, Context context) {
        ArrayList<PushgatewayGaugeEntity> result = new ArrayList<>();
        for (Map.Entry<Integer, String> entry : metricIdxMap.entrySet()) {
            Double metricValue = rowData.getDouble(entry.getKey());
            PushgatewayGaugeEntity e = new PushgatewayGaugeEntity(jobName, entry.getValue(), metricValue, parseTags(rowData));
            setTs(e, rowData);
            result.add(e);
        }
        return result;
    }

    @Override
    protected int getTsIdx() {
        return tsIdx;
    }

    @Override
    protected int getHelpIdx() {
        return helpIdx;
    }

    @Override
    protected Map<Integer, String> getTagsIdxMap() {
        return tagsIdxMap;
    }
}
