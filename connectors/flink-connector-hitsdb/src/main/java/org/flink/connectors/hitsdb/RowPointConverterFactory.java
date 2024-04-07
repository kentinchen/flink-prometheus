package org.flink.connectors.hitsdb;


import org.apache.flink.api.java.typeutils.RowTypeInfo;
import org.flink.connectors.hitsdb.config.NotEnoughParamsException;
import org.flink.connectors.hitsdb.converter.MultiFieldTagSchemaConverter;
import org.flink.connectors.hitsdb.converter.MultiFieldTagSchemalessConverter;
import org.flink.connectors.hitsdb.converter.SingleValueTagSchemaConverter;
import org.flink.connectors.hitsdb.converter.SingleValueTagSchemalessConverter;

import java.util.HashMap;
import java.util.Map;

public class RowPointConverterFactory {
    private static final String METRIC = "metric";
    private static final String TIMESTAMP = "timestamp";
    private static final String VALUE = "value";
    private static final String TAGS = "tags";
    private static final String MULTI_VALUE_FIELD_PREFIX = "field_";

    public static RowPointConverter create(RowTypeInfo rowTypeInfo, boolean checkValid) {
        int metricIdx = rowTypeInfo.getFieldIndex(METRIC);
        int timestampIdx = rowTypeInfo.getFieldIndex(TIMESTAMP);
        if (metricIdx < 0 || timestampIdx < 0)
            throw new NotEnoughParamsException("`metric` and `timestamp` column must be defined correctly");
        int valueIdx = rowTypeInfo.getFieldIndex(VALUE);
        int tagsIdx = rowTypeInfo.getFieldIndex(TAGS);
        String[] fieldNames = rowTypeInfo.getFieldNames();
        Map<Integer, String> fieldsIdxMap = new HashMap<>(fieldNames.length);
        Map<Integer, String> tagsIdxMap = new HashMap<>(fieldNames.length);
        for (int i = 0; i < fieldNames.length; i++) {
            String fieldName = fieldNames[i];
            if (metricIdx != i && timestampIdx != i && valueIdx != i && tagsIdx != i)
                if (fieldName.startsWith(MULTI_VALUE_FIELD_PREFIX)) {
                    fieldsIdxMap.put(i, fieldName.substring(MULTI_VALUE_FIELD_PREFIX.length()));
                } else {
                    tagsIdxMap.put(i, fieldName);
                }
        }
        if (fieldsIdxMap.size() > 0 && valueIdx != -1)
            throw new NotEnoughParamsException("Both single-value and multi-value are defined, choose one.");
        if (fieldsIdxMap.size() == 0 && valueIdx == -1)
            throw new NotEnoughParamsException("Neither single-value or multi-value is defined, choose one.");
        if (tagsIdxMap.size() > 0 && tagsIdx != -1)
            throw new NotEnoughParamsException("Both schema-tags and schemaless-tags are defined, choose one.");
        if (valueIdx != -1) {
            if (tagsIdx != -1)
                return new SingleValueTagSchemalessConverter(metricIdx, timestampIdx, valueIdx, tagsIdx, checkValid);
            return new SingleValueTagSchemaConverter(metricIdx, timestampIdx, valueIdx, tagsIdxMap, checkValid);
        }
        if (tagsIdx != -1)
            return new MultiFieldTagSchemalessConverter(metricIdx, timestampIdx, fieldsIdxMap, tagsIdx, checkValid);
        return new MultiFieldTagSchemaConverter(metricIdx, timestampIdx, fieldsIdxMap, tagsIdxMap, checkValid);
    }
}
