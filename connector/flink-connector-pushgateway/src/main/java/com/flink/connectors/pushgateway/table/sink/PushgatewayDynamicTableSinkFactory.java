package com.flink.connectors.pushgateway.table.sink;

import com.flink.connectors.pushgateway.table.sink.callback.HttpPostRequestCallbackFactory;
import com.flink.connectors.pushgateway.utils.ConfigUtils;
import org.apache.flink.configuration.ConfigOption;
import org.apache.flink.configuration.ReadableConfig;
import org.apache.flink.connector.base.table.AsyncDynamicTableSinkFactory;
import org.apache.flink.connector.base.table.sink.options.AsyncSinkConfigurationValidator;
import org.apache.flink.table.connector.sink.DynamicTableSink;
import org.apache.flink.table.factories.FactoryUtil;

import java.util.Properties;
import java.util.Set;

import static com.flink.connectors.pushgateway.table.sink.PushgatewyaDynamicSinkConnectorOptions.REQUEST_CALLBACK_IDENTIFIER;
import static com.flink.connectors.pushgateway.table.sink.PushgatewyaDynamicSinkConnectorOptions.URL;

public class PushgatewayDynamicTableSinkFactory  extends AsyncDynamicTableSinkFactory {
    public static final String IDENTIFIER = "pushgateway";

    @Override
    public DynamicTableSink createDynamicTableSink(Context context) {
        final AsyncDynamicSinkContext factoryContext = new AsyncDynamicSinkContext(this, context);
        ReadableConfig tableOptions = factoryContext.getTableOptions();

        Properties asyncSinkProperties =
                new AsyncSinkConfigurationValidator(tableOptions).getValidatedConfigurations();

        // generics type erasure, so we have to do an unchecked cast
        final HttpPostRequestCallbackFactory<HttpRequest> postRequestCallbackFactory =
                FactoryUtil.discoverFactory(
                        context.getClassLoader(),
                        HttpPostRequestCallbackFactory.class,  // generics type erasure
                        tableOptions.get(REQUEST_CALLBACK_IDENTIFIER)
                );

        Properties httpConnectorProperties =
                ConfigUtils.getHttpConnectorProperties(context.getCatalogTable().getOptions());

        PushgatewayDynamicSink.HttpDynamicTableSinkBuilder builder =
                new PushgatewayDynamicSink.HttpDynamicTableSinkBuilder()
                        .setTableOptions(tableOptions)
                        .setEncodingFormat(factoryContext.getEncodingFormat())
                        .setHttpPostRequestCallback(
                                postRequestCallbackFactory.createHttpPostRequestCallback()
                        )
                        .setConsumedDataType(factoryContext.getPhysicalDataType())
                        .setProperties(httpConnectorProperties);
        addAsyncOptionsToBuilder(asyncSinkProperties, builder);

        return builder.build();
    }

    @Override
    public String factoryIdentifier() {
        return IDENTIFIER;
    }

    @Override
    public Set<ConfigOption<?>> requiredOptions() {
        return Set.of(URL, FactoryUtil.FORMAT);
    }
}
