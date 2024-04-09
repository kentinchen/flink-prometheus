package com.flink.connectors.pushgateway.table.pushgateway;

import org.apache.flink.configuration.ConfigOption;
import org.apache.flink.configuration.ReadableConfig;
import org.apache.flink.connector.base.table.AsyncDynamicTableSinkFactory;
import org.apache.flink.connector.base.table.sink.options.AsyncSinkConfigurationValidator;
import org.apache.flink.table.connector.sink.DynamicTableSink;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import static com.flink.connectors.pushgateway.table.pushgateway.PushgatewayDynamicSinkConnectorOptions.*;

public class PushgatewayDynamicTableSinkFactory extends AsyncDynamicTableSinkFactory {
    public static final String IDENTIFIER = "pushgateway";

    @Override
    public DynamicTableSink createDynamicTableSink(Context context) {
        final AsyncDynamicSinkContext factoryContext = new AsyncDynamicSinkContext(this, context);
        ReadableConfig tableOptions = factoryContext.getTableOptions();
        Properties asyncSinkProperties = new AsyncSinkConfigurationValidator(tableOptions).getValidatedConfigurations();
        // generics type erasure, so we have to do an unchecked cast
        PushgatewayDynamicSink.PushgatewayDynamicTableSinkBuilder builder =
                new PushgatewayDynamicSink.PushgatewayDynamicTableSinkBuilder()
                        .setTableOptions(tableOptions)
                        .setEncodingFormat(factoryContext.getEncodingFormat())
                        .setConsumedDataType(factoryContext.getPhysicalDataType());
        addAsyncOptionsToBuilder(asyncSinkProperties, builder);
        return builder.build();
    }

    @Override
    public String factoryIdentifier() {
        return IDENTIFIER;
    }

    @Override
    public Set<ConfigOption<?>> requiredOptions() {
        Set<ConfigOption<?>> options = new HashSet<>();
        options.add(PUSHGATEWAY);
        return options;
    }

    @Override
    public Set<ConfigOption<?>> optionalOptions() {
        Set<ConfigOption<?>> options = super.optionalOptions();
        options.add(PROM_JOB);
        options.add(PROM_M_TYPE);
        options.add(PROM_J_NAME);
        options.add(PROM_M_NAME);
        options.add(PROM_V_NAME);
        options.add(PROM_H_NAME);
        options.add(PROM_TS_NAME);
        return options;
    }
}
