package com.flink.connectors.pushgateway.sink;

import java.util.Properties;

public interface RequestSubmitterFactory {
    RequestSubmitter createSubmitter(Properties properties, String[] headersAndValues);
}
