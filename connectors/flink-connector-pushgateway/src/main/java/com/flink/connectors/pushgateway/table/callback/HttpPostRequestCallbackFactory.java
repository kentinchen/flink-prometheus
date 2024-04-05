package com.flink.connectors.pushgateway.table.callback;

import org.apache.flink.table.factories.Factory;

public interface HttpPostRequestCallbackFactory<RequestT> extends Factory {
    HttpPostRequestCallback<RequestT> createHttpPostRequestCallback();
}
