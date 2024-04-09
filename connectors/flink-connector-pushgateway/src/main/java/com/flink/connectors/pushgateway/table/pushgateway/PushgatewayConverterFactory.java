package com.flink.connectors.pushgateway.table.pushgateway;

import com.flink.connectors.pushgateway.config.NotEnoughParamsException;
import org.apache.flink.api.common.serialization.SerializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.configuration.ReadableConfig;
import org.apache.flink.table.data.RowData;
import org.apache.flink.table.types.DataType;
import org.apache.flink.table.types.logical.RowType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.flink.connectors.pushgateway.table.pushgateway.PushgatewayDynamicSinkConnectorOptions.*;

public class PushgatewayConverterFactory {
    private static String M_TYPE = "m_type";
    private static String M_NAME = "m_name";
    private static String M_VALUE = "m_value";
    private static String TIMESTAMP = "timestamp";

    public static SerializationSchemaElementConverter create(ReadableConfig tableOptions,DataType consumedDataType ,SerializationSchema<RowData> serializationSchema) {
        final RowType physicalRowType = (RowType) consumedDataType.getLogicalType();
        List<RowType.RowField> fields = physicalRowType.getFields();
        Map<Integer, String> tagsIdxMap = new HashMap<>();
        String[] fieldNames = new String[fields.size()];
        TypeInformation[] fieldTypes = new TypeInformation[fields.size()];
        String jobName = String.valueOf(tableOptions.getOptional(PROM_JOB));
        String type = String.valueOf(tableOptions.getOptional(PROM_TYPE));
        int jobIdx = -1;
        int typeIdx = -1;
        int nameIdx = -1;
        int helpIdx = -1;
        int valueIdx = -1;
        int tsIdx = -1;
        for (int i = 0; i < physicalRowType.getFieldCount(); i++) {
            String fieldName = fields.get(i).getName();
            System.out.println("i:" + i + " fieldName:" + fieldName + " type:" + fields.get(i).getType());
            if (fieldName.equals(String.valueOf(tableOptions.getOptional(PROM_J_NAME)))) {
                jobIdx = i;
            } else if (fieldName.equals(String.valueOf(tableOptions.getOptional(PROM_M_NAME)))) {
                nameIdx = i;
            } else if (fieldName.equals(String.valueOf(tableOptions.getOptional(PROM_H_NAME)))) {
                helpIdx = i;
            } else if (fieldName.equals(String.valueOf(tableOptions.getOptional(PROM_V_NAME)))) {
                valueIdx = i;
            } else if (fieldName.equals(String.valueOf(tableOptions.getOptional(PROM_TS_NAME)))) {
                tsIdx = i;
            } else {
                tagsIdxMap.put(i, fieldName);
            }
            fieldNames[i] = fieldName;
        }
        if (nameIdx < 0 || valueIdx < 0) {
            throw new NotEnoughParamsException("`metric` and `value` column must be defined correctly");
        }
        return new SerializationSchemaElementConverter(jobName, type,jobIdx,typeIdx,
                nameIdx, helpIdx,valueIdx, tsIdx, tagsIdxMap,serializationSchema);
    }
}
