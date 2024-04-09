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
                    .withDescription("指标推送地址，例如pushgateway:9091");

    public static final ConfigOption<String> PROM_JOB = ConfigOptions.key(CONST_PROM_JOB).stringType()
            .defaultValue("flink").withDescription("作业标符");
    public static final ConfigOption<String> PROM_TYPE = ConfigOptions.key(CONST_PROM_TYPE).stringType()
            .defaultValue("gauge").withDescription("指标类型");
    public static final ConfigOption<String> PROM_J_NAME = ConfigOptions.key(CONST_PROM_J_NAME).stringType()
            .defaultValue(CONST_J).withDescription("作业名字段名");
    public static final ConfigOption<String> PROM_M_TYPE = ConfigOptions.key(CONST_PROM_M_TYPE).stringType()
            .defaultValue("").withDescription("指标类型字段名");
    public static final ConfigOption<String> PROM_M_NAME = ConfigOptions.key(CONST_PROM_M_NAME).stringType()
            .defaultValue(CONST_M).withDescription("指标名字段名");
    public static final ConfigOption<String> PROM_V_NAME = ConfigOptions.key(CONST_PROM_V_NAME).stringType()
            .defaultValue(CONST_V).withDescription("指标值字段名");
    public static final ConfigOption<String> PROM_H_NAME = ConfigOptions.key(CONST_PROM_H_NAME).stringType()
            .defaultValue(CONST_H).withDescription("帮助字段名");
    public static final ConfigOption<String> PROM_TS_NAME = ConfigOptions.key(CONST_PROM_TS_NAME).stringType()
            .defaultValue(CONST_TS).withDescription("时间戳字段名");
}
