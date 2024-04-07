package org.flink.connectors.hitsdb.converter;

import org.apache.flink.types.Row;

import java.util.Map;

public class SingleValueTagSchemalessConverter extends AbstractSingleValueConverter {
    private int tagsIdx;

    public SingleValueTagSchemalessConverter(int metricIdx, int timestampIdx, int valueIdx, int tagsIdx, boolean checkValid) {
        super(metricIdx, timestampIdx, valueIdx, checkValid);
        this.tagsIdx = tagsIdx;
    }

    protected Map<String, String> parseTags(Row row) {
        return parseTags(this.tagsIdx, row);
    }
}
