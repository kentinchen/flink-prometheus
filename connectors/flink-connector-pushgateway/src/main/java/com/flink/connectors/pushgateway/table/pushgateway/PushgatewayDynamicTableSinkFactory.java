package com.flink.connectors.pushgateway.table.pushgateway;

import com.flink.connectors.pushgateway.sink.httpclient.HttpRequest;
import com.flink.connectors.pushgateway.table.callback.HttpPostRequestCallbackFactory;
import com.flink.connectors.pushgateway.utils.ConfigUtils;
import org.apache.flink.configuration.ConfigOption;
import org.apache.flink.configuration.ReadableConfig;
import org.apache.flink.connector.base.table.AsyncDynamicTableSinkFactory;
import org.apache.flink.connector.base.table.sink.options.AsyncSinkConfigurationValidator;
import org.apache.flink.table.connector.sink.DynamicTableSink;
import org.apache.flink.table.factories.FactoryUtil;

import java.util.Properties;
import java.util.Set;

import static com.flink.connectors.pushgateway.table.pushgateway.PushgatewyaDynamicSinkConnectorOptions.PUSHGATEWAY;
import static com.flink.connectors.pushgateway.table.pushgateway.PushgatewyaDynamicSinkConnectorOptions.REQUEST_CALLBACK_IDENTIFIER;

public class PushgatewayDynamicTableSinkFactory extends AsyncDynamicTableSinkFactory {
    public static final String IDENTIFIER = "pushgateway";

    @Override
    public DynamicTableSink createDynamicTableSink(Context context) {
        final AsyncDynamicSinkContext factoryContext = new AsyncDynamicSinkContext(this, context);
        ReadableConfig tableOptions = factoryContext.getTableOptions();
        Properties asyncSinkProperties = new AsyncSinkConfigurationValidator(tableOptions).getValidatedConfigurations();
        // generics type erasure, so we have to do an unchecked cast
        final HttpPostRequestCallbackFactory<HttpRequest> requestCallbackFactory =
                FactoryUtil.discoverFactory(context.getClassLoader(),
                        HttpPostRequestCallbackFactory.class,  // generics type erasure
                        tableOptions.get(REQUEST_CALLBACK_IDENTIFIER));
        Properties connectorProperties = ConfigUtils.getHttpConnectorProperties(context.getCatalogTable().getOptions());
        PushgatewayDynamicSink.PushgatewayDynamicTableSinkBuilder builder =
                new PushgatewayDynamicSink.PushgatewayDynamicTableSinkBuilder()
                        .setTableOptions(tableOptions)
                        .setEncodingFormat(factoryContext.getEncodingFormat())
                        .setHttpPostRequestCallback(requestCallbackFactory.createHttpPostRequestCallback())
                        .setConsumedDataType(factoryContext.getPhysicalDataType())
                        .setProperties(connectorProperties);
        addAsyncOptionsToBuilder(asyncSinkProperties, builder);
        return builder.build();
    }

    @Override
    public String factoryIdentifier() {
        return IDENTIFIER;
    }

    @Override
    public Set<ConfigOption<?>> requiredOptions() {
        return Set.of(PUSHGATEWAY);
    }

    @Override
    public Set<ConfigOption<?>> optionalOptions() {
        var options = super.optionalOptions();
        options.add(REQUEST_CALLBACK_IDENTIFIER);
        return options;
    }
}
