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
            .defaultValue(CONST_FLINK).withDescription("作业标符");
    public static final ConfigOption<String> PROM_METHOD = ConfigOptions.key(CONST_PROM_METHOD).stringType()
            .defaultValue("").withDescription("推送方式标符,direct直接推送,其它用SDK推送");
    public static final ConfigOption<Integer> PROM_TTL = ConfigOptions.key(CONST_PROM_TTL).intType()
            .defaultValue(0).withDescription("指标推送后存留时长(分钟)");
    public static final ConfigOption<Integer> PROM_BATCH_SIZE = ConfigOptions.key(CONST_BATCH_SIZE).intType()
            .defaultValue(100).withDescription("调试开关，默认f");
    public static final ConfigOption<String> PROM_TYPE = ConfigOptions.key(CONST_PROM_TYPE).stringType()
            .defaultValue(CONST_GAUGE).withDescription("指标类型");
    public static final ConfigOption<String> PROM_DEBUG = ConfigOptions.key(CONST_PROM_DEBUG).stringType()
            .defaultValue(CONST_F).withDescription("调试开关，默认f");
    public static final ConfigOption<String> PROM_MODE = ConfigOptions.key(CONST_PROM_MODE).stringType()
            .defaultValue(CONST_ROW).withDescription("指标模式，默认row");
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
