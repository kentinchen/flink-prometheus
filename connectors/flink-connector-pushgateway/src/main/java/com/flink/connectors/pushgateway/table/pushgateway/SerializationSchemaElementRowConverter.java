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
public class SerializationSchemaElementRowConverter extends AbstractElementColumnConverter
        implements SchemaLifecycleAwareElementConverter<RowData, ArrayList<PushgatewayGaugeEntity>> {
    protected final int tsIdx;
    protected final int helpIdx;
    private final SerializationSchema<RowData> serializationSchema;
    private final String jobName;
    private final int metricIdx;
    private final int valueIdx;
    private final Map<Integer, String> tagsIdxMap;
    private boolean schemaOpened = false;


    public SerializationSchemaElementRowConverter(String jobName, int metricIdx, int helpIdx, int valueIdx, int tsIdx, Map<Integer, String> tagsIdxMap, SerializationSchema<RowData> serializationSchema) {
        this.jobName = jobName;
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
    public ArrayList<PushgatewayGaugeEntity> apply(RowData rowData, Context context) {
        String metricName1 = rowData.getString(metricIdx).toString();
        String metricName = metricName1.replaceAll("-", "_")
                .replaceAll("\\.", "_");
        Double metricValue = rowData.getDouble(valueIdx);
        PushgatewayGaugeEntity e = new PushgatewayGaugeEntity(jobName, metricName, metricValue, parseTags(rowData));
        setTs(e, rowData);
        setHelp(e, rowData);
        ArrayList<PushgatewayGaugeEntity> result = new ArrayList<>();
        result.add(e);
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
