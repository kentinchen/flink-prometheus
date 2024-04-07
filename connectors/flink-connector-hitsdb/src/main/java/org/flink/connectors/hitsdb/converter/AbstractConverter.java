package org.flink.connectors.hitsdb.converter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.apache.flink.types.Row;
import org.flink.connectors.hitsdb.RowPointConverter;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public abstract class AbstractConverter implements RowPointConverter {
    protected int metricIdx;
    protected int timestampIdx;

    protected boolean checkValid;

    public AbstractConverter(int metricIdx, int timestampIdx, boolean checkValid) {
        this.metricIdx = metricIdx;
        this.timestampIdx = timestampIdx;
        this.checkValid = checkValid;
    }

    protected String parseMetric(Row row) {
        return row.getField(this.metricIdx).toString();
    }

    protected long parseTimestamp(Row row) {
        return ((Number) row.getField(this.timestampIdx)).longValue();
    }

    protected abstract Map<String, String> parseTags(Row paramRow);

    protected Map<String, String> parseTags(int tagsIdx, Row row) {
        String tagsStr = (String) row.getField(tagsIdx);
        return JSON.parseObject(tagsStr, new TypeReference<Map<String, String>>() {
        }, new com.alibaba.fastjson.parser.Feature[0]);
    }

    protected Map<String, String> parseTags(Map<Integer, String> tagsIdxMap, Row row) {
        return tagsIdxMap.isEmpty() ? Collections.emptyMap() : doParseTags(tagsIdxMap, row);
    }

    private Map<String, String> doParseTags(Map<Integer, String> tagsIdxMap, Row row) {
        Map<String, String> tags = new TreeMap<>();
        for (Map.Entry<Integer, String> entry : tagsIdxMap.entrySet()) {
            int idx = entry.getKey();
            String tagKey = entry.getValue();
            Object rawTagValue = row.getField(idx);
            if (rawTagValue == null)
                throw new IllegalStateException("the tag value is null for tag key:" + tagKey);
            String tagValue = rawTagValue.toString();
            tags.put(tagKey, tagValue);
        }
        return tags;
    }

    protected Object parseValue(int valueIdx, Row row) {
        return row.getField(valueIdx);
    }

    protected Map<String, Object> parseFields(Map<Integer, String> fieldsIdxMap, Row row) {
        Map<String, Object> fields = new TreeMap<>();
        for (Map.Entry<Integer, String> entry : fieldsIdxMap.entrySet()) {
            int idx = entry.getKey();
            String fieldName = entry.getValue();
            Object fieldValue = row.getField(idx);
            if (fieldValue == null)
                throw new IllegalStateException("the field value is null for field name:" + fieldName);
            fields.put(fieldName, fieldValue);
        }
        return fields;
    }
}
