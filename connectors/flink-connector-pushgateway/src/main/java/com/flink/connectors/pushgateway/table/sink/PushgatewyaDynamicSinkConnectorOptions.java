package com.flink.connectors.pushgateway.table.sink;

import com.flink.connectors.pushgateway.table.sink.callback.Slf4jHttpPostRequestCallbackFactory;
import org.apache.flink.configuration.ConfigOption;
import org.apache.flink.configuration.ConfigOptions;

import static com.flink.connectors.pushgateway.config.ConnectorConfigConstants.SINK_REQUEST_CALLBACK_IDENTIFIER;

/**
 * Table API options for {@link PushgatewayDynamicSink}.
 */
public class PushgatewyaDynamicSinkConnectorOptions {

    public static final ConfigOption<String> URL =
        ConfigOptions.key("url").stringType().noDefaultValue()
            .withDescription("The HTTP endpoint URL.");

    public static final ConfigOption<String> INSERT_METHOD =
        ConfigOptions.key("insert-method")
            .stringType()
            .defaultValue("POST")
            .withDescription("Method used for requests built from SQL's INSERT.");

    public static final ConfigOption<String> REQUEST_CALLBACK_IDENTIFIER =
        ConfigOptions.key(SINK_REQUEST_CALLBACK_IDENTIFIER)
            .stringType()
            .defaultValue(Slf4jHttpPostRequestCallbackFactory.IDENTIFIER);
}
