package com.flink.connectors.pushgateway.table.pushgateway;

import com.flink.connectors.pushgateway.table.callback.Slf4jHttpPostRequestCallbackFactory;
import org.apache.flink.configuration.ConfigOption;
import org.apache.flink.configuration.ConfigOptions;

import static com.flink.connectors.pushgateway.config.ConnectorConfigConstants.SINK_REQUEST_CALLBACK_IDENTIFIER;

/**
 * Table API options for {@link PushgatewayDynamicSink}.
 */
public class PushgatewyaDynamicSinkConnectorOptions {

    public static final ConfigOption<String> PUSHGATEWAY =
            ConfigOptions.key("pushgateway").stringType().noDefaultValue()
                    .withDescription("The pushgateway Url,for example：pushgateway:9091");

    public static final ConfigOption<String> REQUEST_CALLBACK_IDENTIFIER =
            ConfigOptions.key(SINK_REQUEST_CALLBACK_IDENTIFIER)
                    .stringType()
                    .defaultValue(Slf4jHttpPostRequestCallbackFactory.IDENTIFIER);
}
