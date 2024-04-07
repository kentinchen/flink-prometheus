package org.flink.connectors.hitsdb.converter;

import org.apache.flink.types.Row;

import java.util.Map;

public class MultiFieldTagSchemalessConverter extends AbstractMultiFieldConverter {
    protected int tagsIdx;

    public MultiFieldTagSchemalessConverter(int metricIdx, int timestampIdx, Map<Integer, String> fieldsIdxMap, int tagsIdx, boolean checkValid) {
        super(metricIdx, timestampIdx, fieldsIdxMap, checkValid);
        this.tagsIdx = tagsIdx;
    }

    protected Map<String, String> parseTags(Row row) {
        return parseTags(this.tagsIdx, row);
    }
}
