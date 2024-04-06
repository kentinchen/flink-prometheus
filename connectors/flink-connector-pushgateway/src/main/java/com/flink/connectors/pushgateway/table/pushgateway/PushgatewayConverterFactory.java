package com.flink.connectors.pushgateway.table.pushgateway;

import com.flink.connectors.pushgateway.config.NotEnoughParamsException;
import org.apache.flink.api.common.serialization.SerializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.table.data.RowData;
import org.apache.flink.table.types.DataType;
import org.apache.flink.table.types.logical.RowType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PushgatewayConverterFactory {
    private static String M_TYPE = "m_type";
    private static String M_NAME = "m_name";
    private static String M_VALUE = "m_value";
    private static String TIMESTAMP = "timestamp";

    public static SerializationSchemaElementConverter create(DataType consumedDataType) {
        final RowType physicalRowType = (RowType) consumedDataType.getLogicalType();
        List<RowType.RowField> fields = physicalRowType.getFields();
        Map<Integer, String> tagsIdxMap = new HashMap<>();
        String[] fieldNames = new String[fields.size()];
        TypeInformation[] fieldTypes = new TypeInformation[fields.size()];
        int typeIdx = -1;
        int nameIdx = -1;
        int valueIdx = -1;
        int tsIdx = -1;
        for (int i = 0; i < physicalRowType.getFieldCount(); i++) {
            String fieldName = fields.get(i).getName();
            System.out.println("i:" + i + " fieldName:" + fieldName + " type:" + fields.get(i).getType());
            if (fieldName.equals(M_TYPE)) {
                typeIdx = i;
            } else if (fieldName.equals(M_NAME)) {
                nameIdx = i;
            } else if (fieldName.equals(M_VALUE)) {
                valueIdx = i;
            } else if (fieldName.equals(TIMESTAMP)) {
                tsIdx = i;
            } else {
                tagsIdxMap.put(i, fieldName);
            }
            fieldNames[i] = fieldName;
        }
        if (nameIdx < 0 || valueIdx < 0) {
            throw new NotEnoughParamsException("`metric` and `timestamp` column must be defined correctly");
        }
        return new SerializationSchemaElementConverter(nameIdx, valueIdx, tagsIdxMap);
    }
}
