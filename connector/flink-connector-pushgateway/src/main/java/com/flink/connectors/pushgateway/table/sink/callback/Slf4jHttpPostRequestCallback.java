package com.flink.connectors.pushgateway.table.sink.callback;

import com.flink.connectors.pushgateway.table.sink.HttpRequest;
import com.flink.connectors.pushgateway.table.sink.PushgatewayDynamicSink;
import com.flink.connectors.pushgateway.utils.ConfigUtils;
import lombok.extern.slf4j.Slf4j;

import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A {@link HttpPostRequestCallback} that logs pairs of request and response as <i>INFO</i> level
 * logs using <i>Slf4j</i>.
 *
 * <p>Serving as a default implementation of {@link HttpPostRequestCallback} for
 * the {@link PushgatewayDynamicSink}.
 */
@Slf4j
public class Slf4jHttpPostRequestCallback implements HttpPostRequestCallback<HttpRequest> {

    @Override
    public void call(
        HttpResponse<String> response,
        HttpRequest requestEntry,
        String endpointUrl,
        Map<String, String> headerMap) {

        String requestBody = requestEntry.getElements().stream()
            .map(element -> new String(element, StandardCharsets.UTF_8))
            .collect(Collectors.joining());

        if (response == null) {
            log.info(
                "Got response for a request.\n  Request:\n    " +
                "Method: {}\n    Body: {}\n  Response: null",
                requestEntry.getMethod(),
                requestBody
            );
        } else {
            log.info(
                "Got response for a request.\n  Request:\n    " +
                "Method: {}\n    Body: {}\n  Response: {}\n    Body: {}",
                requestEntry.method,
                requestBody,
                response,
                response.body().replaceAll(ConfigUtils.UNIVERSAL_NEW_LINE_REGEXP, "")
            );
        }
    }
}
