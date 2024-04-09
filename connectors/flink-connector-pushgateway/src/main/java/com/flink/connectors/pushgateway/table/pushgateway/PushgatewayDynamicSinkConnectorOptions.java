package com.flink.connectors.pushgateway.table.pushgateway;

import org.apache.flink.configuration.ConfigOption;
import org.apache.flink.configuration.ConfigOptions;

import static com.flink.connectors.pushgateway.config.ConnectorConfigConstants.*;

/**
 * Table API options for {@link PushgatewayDynamicSink}.
 */
public class PushgatewayDynamicSinkConnectorOptions {
    public static final ConfigOption<String> PUSHGATEWAY =
            ConfigOptions.key("pushgateway").stringType().noDefaultValue()
                    .withDescription("The pushgateway Url,for example：pushgateway:9091");

    public static final ConfigOption<String> PROM_JOB = ConfigOptions.key(CONST_PROM_JOB).stringType()
            .defaultValue("flink").withDescription("作业标符");
    public static final ConfigOption<String> PROM_M_TYPE = ConfigOptions.key(CONST_PROM_M_TYPE).stringType()
            .defaultValue("m");
    public static final ConfigOption<String> PROM_J_NAME = ConfigOptions.key(CONST_PROM_J_NAME).stringType()
            .defaultValue("");
    public static final ConfigOption<String> PROM_M_NAME = ConfigOptions.key(CONST_PROM_M_NAME).stringType()
            .defaultValue("m");
    public static final ConfigOption<String> PROM_V_NAME = ConfigOptions.key(CONST_PROM_V_NAME).stringType()
            .defaultValue("v");
    public static final ConfigOption<String> PROM_H_NAME = ConfigOptions.key(CONST_PROM_H_NAME).stringType()
            .defaultValue("h");
    public static final ConfigOption<String> PROM_TS_NAME = ConfigOptions.key(CONST_PROM_TS_NAME).stringType()
            .defaultValue("ts");
}
