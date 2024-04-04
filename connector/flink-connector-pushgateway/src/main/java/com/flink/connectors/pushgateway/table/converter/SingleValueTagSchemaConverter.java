package com.flink.connectors.pushgateway.table.converter;

import org.apache.flink.types.Row;

import java.util.Map;

public class SingleValueTagSchemaConverter extends AbstractSingleValueConverter {
    protected Map<Integer, String> tagsIdxMap;

    public SingleValueTagSchemaConverter(int metricIdx, int timestampIdx, int valueIdx, Map<Integer, String> tagsIdxMap, boolean checkValid) {
        super(metricIdx, timestampIdx, valueIdx, checkValid);
        this.tagsIdxMap = tagsIdxMap;
    }

    protected Map<String, String> parseTags(Row row) {
        return parseTags(this.tagsIdxMap, row);
    }
}
