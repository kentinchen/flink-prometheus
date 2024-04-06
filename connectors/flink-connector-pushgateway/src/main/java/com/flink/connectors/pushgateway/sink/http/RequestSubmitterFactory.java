package com.flink.connectors.pushgateway.sink.http;

import java.util.Properties;

public interface RequestSubmitterFactory {
    RequestSubmitter createSubmitter(Properties properties, String[] headersAndValues);
}
