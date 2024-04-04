package com.flink.connectors.pushgateway.table.sink;

import org.apache.flink.configuration.ConfigOption;
import org.apache.flink.connector.base.table.AsyncDynamicTableSinkFactory;
import org.apache.flink.table.connector.sink.DynamicTableSink;

import java.util.Set;

public class PushgatewayDynamicTableSinkFactory  extends AsyncDynamicTableSinkFactory {
    @Override
    public DynamicTableSink createDynamicTableSink(Context context) {
        return null;
    }

    @Override
    public String factoryIdentifier() {
        return "";
    }

    @Override
    public Set<ConfigOption<?>> requiredOptions() {
        return Set.of();
    }
}
