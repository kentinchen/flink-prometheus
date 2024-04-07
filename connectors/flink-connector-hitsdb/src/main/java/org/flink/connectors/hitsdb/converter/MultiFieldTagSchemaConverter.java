package org.flink.connectors.hitsdb.converter;

import org.apache.flink.types.Row;

import java.util.Map;

public class MultiFieldTagSchemaConverter extends AbstractMultiFieldConverter {
    protected Map<Integer, String> tagsIdxMap;

    public MultiFieldTagSchemaConverter(int metricIdx, int timestampIdx, Map<Integer, String> fieldsIdxMap, Map<Integer, String> tagsIdxMap, boolean checkValid) {
        super(metricIdx, timestampIdx, fieldsIdxMap, checkValid);
        this.tagsIdxMap = tagsIdxMap;
    }

    protected Map<String, String> parseTags(Row row) {
        return parseTags(this.tagsIdxMap, row);
    }
}
