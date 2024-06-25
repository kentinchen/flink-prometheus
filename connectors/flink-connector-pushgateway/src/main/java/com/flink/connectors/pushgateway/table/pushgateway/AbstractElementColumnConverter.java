package com.flink.connectors.pushgateway.table.pushgateway;

import com.flink.connectors.pushgateway.sink.pushgateway.PushgatewayGaugeEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.table.data.RowData;

import java.util.Map;
import java.util.TreeMap;

@Slf4j
public abstract class AbstractElementColumnConverter {
    public AbstractElementColumnConverter() {
    }

    protected abstract int getTsIdx();

    protected abstract int getHelpIdx();

    protected void setTs(PushgatewayGaugeEntity e, RowData rowData) {
        if (getTsIdx() > 0) {
            long ts = rowData.getLong(getTsIdx());
            if (Long.toString(ts).length() > 10) {
                ts = ts / 1000L;
            }
            e.setTimestamp(ts);
        }
    }

    protected void setHelp(PushgatewayGaugeEntity e, RowData rowData) {
        if (getHelpIdx() > 0) {
            e.setMetricHelp(rowData.getString(getHelpIdx()).toString());
        }
    }

    protected abstract Map<Integer, String> getTagsIdxMap();

    protected TreeMap<String, String> parseTags(RowData row) {
        return getTagsIdxMap().isEmpty() ? new TreeMap<>() : doParseTags(getTagsIdxMap(), row);
    }

    protected TreeMap<String, String> doParseTags(Map<Integer, String> tagsIdxMap, RowData row) {
        TreeMap<String, String> tags = new TreeMap<>();
        for (Map.Entry<Integer, String> entry : tagsIdxMap.entrySet()) {
            int idx = entry.getKey();
            String tagKey = entry.getValue();
            try {
                Object rawTagValue = row.getString(idx);
                if (rawTagValue != null && !rawTagValue.toString().equals("")) {
                    tags.put(tagKey, rawTagValue.toString());
                }
            } catch (Exception e) {
                log.error("错误tag列,tag idx:" + idx + " tagKey:" + tagKey);
            }
        }
        return tags;
    }

}
