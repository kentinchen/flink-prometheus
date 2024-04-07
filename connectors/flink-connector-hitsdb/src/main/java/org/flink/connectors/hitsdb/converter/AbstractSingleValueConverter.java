package org.flink.connectors.hitsdb.converter;

import com.aliyun.hitsdb.client.value.request.AbstractPoint;
import com.aliyun.hitsdb.client.value.request.Point;
import org.apache.flink.types.Row;

import java.util.Map;

public abstract class AbstractSingleValueConverter extends AbstractConverter {
    protected int valueIdx;

    public AbstractSingleValueConverter(int metricIdx, int timestampIdx, int valueIdx, boolean checkValid) {
        super(metricIdx, timestampIdx, checkValid);
        this.valueIdx = valueIdx;
    }

    public AbstractPoint convert(Row row) {
        String metric = parseMetric(row);
        long timestamp = parseTimestamp(row);
        Map<String, String> tags = parseTags(row);
        Object value = parseValue(this.valueIdx, row);
        return Point.metric(metric).timestamp(timestamp).value(value).tag(tags).build(this.checkValid);
    }
}
