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

import static com.flink.connectors.pushgateway.config.ConnectorConfigConstants.*;
import static com.flink.connectors.pushgateway.table.pushgateway.PushgatewayDynamicSinkConnectorOptions.*;

public class PushgatewayConverterFactory {

    public static SerializationSchemaElementConverter create(ReadableConfig tableOptions,DataType consumedDataType ,SerializationSchema<RowData> serializationSchema) {
        final RowType physicalRowType = (RowType) consumedDataType.getLogicalType();
        List<RowType.RowField> fields = physicalRowType.getFields();
        Map<Integer, String> tagsIdxMap = new HashMap<>();
        String[] fieldNames = new String[fields.size()];
        TypeInformation[] fieldTypes = new TypeInformation[fields.size()];
        String jobName = tableOptions.getOptional(PROM_JOB).orElse(CONST_FLINK);
        String type = tableOptions.getOptional(PROM_TYPE).orElse(CONST_GAUGE);
        int jobIdx = -1;
        int typeIdx = -1;
        int nameIdx = -1;
        int helpIdx = -1;
        int valueIdx = -1;
        int tsIdx = -1;
        String metricName = tableOptions.getOptional(PROM_M_NAME).orElse(CONST_M);
        System.out.println("PROM_M_NAME:" + metricName);
        String metricValue = tableOptions.getOptional(PROM_V_NAME).orElse(CONST_V);
        System.out.println("PROM_V_NAME:" + metricValue);
        for (int i = 0; i < physicalRowType.getFieldCount(); i++) {
            String fieldName = fields.get(i).getName();
            System.out.println("i:" + i + " fieldName:" + fieldName + " type:" + fields.get(i).getType());
            if (fieldName.equals(tableOptions.getOptional(PROM_J_NAME).orElse(CONST_J))) {
                jobIdx = i;
            } else if (fieldName.equals(metricName)) {
                nameIdx = i;
            } else if (fieldName.equals(tableOptions.getOptional(PROM_H_NAME).orElse(CONST_H))) {
                helpIdx = i;
            } else if (fieldName.equals(metricValue)) {
                valueIdx = i;
            } else if (fieldName.equals(tableOptions.getOptional(PROM_TS_NAME).orElse(CONST_TS))) {
                tsIdx = i;
            } else {
                tagsIdxMap.put(i, fieldName);
            }
            fieldNames[i] = fieldName;
        }
        if (nameIdx < 0 || valueIdx < 0) {
            throw new NotEnoughParamsException("指标名(默认m)及指标值字段(默认v)不存在");
        }
        return new SerializationSchemaElementConverter(jobName, type,jobIdx,typeIdx,
                nameIdx, helpIdx,valueIdx, tsIdx, tagsIdxMap,serializationSchema);
    }
}
