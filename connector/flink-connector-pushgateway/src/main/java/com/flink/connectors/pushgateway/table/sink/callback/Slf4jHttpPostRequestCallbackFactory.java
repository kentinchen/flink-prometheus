package com.flink.connectors.pushgateway.table.sink.callback;

import com.flink.connectors.pushgateway.table.sink.HttpRequest;
import org.apache.flink.configuration.ConfigOption;

import java.util.HashSet;
import java.util.Set;

/**
 * Factory for creating {@link Slf4jHttpPostRequestCallback}.
 */
public class Slf4jHttpPostRequestCallbackFactory
    implements HttpPostRequestCallbackFactory<HttpRequest> {

    public static final String IDENTIFIER = "slf4j-logger";

    @Override
    public HttpPostRequestCallback<HttpRequest> createHttpPostRequestCallback() {
        return new Slf4jHttpPostRequestCallback();
    }

    @Override
    public String factoryIdentifier() {
        return IDENTIFIER;
    }

    @Override
    public Set<ConfigOption<?>> requiredOptions() {
        return new HashSet<>();
    }

    @Override
    public Set<ConfigOption<?>> optionalOptions() {
        return new HashSet<>();
    }
}
