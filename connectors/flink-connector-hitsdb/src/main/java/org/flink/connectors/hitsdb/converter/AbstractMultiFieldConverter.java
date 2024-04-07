package org.flink.connectors.hitsdb.converter;

import com.aliyun.hitsdb.client.value.request.AbstractPoint;
import com.aliyun.hitsdb.client.value.request.MultiFieldPoint;
import org.apache.flink.types.Row;

import java.util.Map;

public abstract class AbstractMultiFieldConverter extends AbstractConverter {
    protected Map<Integer, String> fieldsIdxMap;

    public AbstractMultiFieldConverter(int metricIdx, int timestampIdx, Map<Integer, String> fieldsIdxMap, boolean checkValid) {
        super(metricIdx, timestampIdx, checkValid);
        this.fieldsIdxMap = fieldsIdxMap;
    }

    public AbstractPoint convert(Row row) {
        String metric = parseMetric(row);
        long timestamp = parseTimestamp(row);
        Map<String, String> tags = parseTags(row);
        Map<String, Object> fields = parseFields(this.fieldsIdxMap, row);
        return MultiFieldPoint.metric(metric).timestamp(timestamp).tags(tags).fields(fields).build(this.checkValid);
    }
}
