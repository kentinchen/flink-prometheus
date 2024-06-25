package com.flink.connectors.pushgateway.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.experimental.UtilityClass;


@UtilityClass
@NoArgsConstructor(access = AccessLevel.NONE)
public final class ConnectorConfigConstants {
    public static final String PROP_DELIM = ",";

    public static final String CONST_PROM_JOB = "prom.job";
    public static final String CONST_PROM_TYPE = "prom.type";
    public static final String CONST_PROM_DEBUG = "prom.debug";
    public static final String CONST_PROM_GROUP = "prom.group";
    public static final String CONST_PROM_MODE = "prom.mode";
    public static final String CONST_PROM_TTL = "prom.ttl";
    public static final String CONST_PROM_METHOD = "prom.method";
    public static final String CONST_BATCH_SIZE = "prom.batch_size";
    public static final String CONST_PROM_M_TYPE = "prom.m.type";
    public static final String CONST_PROM_J_NAME = "prom.j.name";
    public static final String CONST_PROM_M_NAME = "prom.m.name";
    public static final String CONST_PROM_V_NAME = "prom.v.name";
    public static final String CONST_PROM_H_NAME = "prom.h.name";
    public static final String CONST_PROM_TS_NAME = "prom.ts.name";

    public static final String CONST_FLINK = "flink";
    public static final String CONST_GAUGE = "gauge";
    public static final String CONST_M = "m";
    public static final String CONST_V = "v";
    public static final String CONST_J = "j";
    public static final String CONST_H = "h";
    public static final String CONST_TS = "ts";

    public static final String CONST_T = "t";
    public static final String CONST_F = "f";

    public static final String CONST_ROW = "row";

    public static final String CONST_PRODUCT = "product";
    public static final String CONST_COLUMN = "column";

    public static final String CONST_DIRECT = "direct";
    public static final String CONST_REGISTRY = "registry";

    public static final String PRODUCT_INSTANCEID_COLUMN = "product,instanceId";
}
