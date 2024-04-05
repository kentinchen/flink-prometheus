package com.flink.connectors.pushgateway.table.callback;

import com.flink.connectors.pushgateway.sink.httpclient.HttpRequest;
import com.flink.connectors.pushgateway.utils.ConfigUtils;
import lombok.extern.slf4j.Slf4j;

import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

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
            log.info("Got response for a request.\n  Request:\n    Method: {}\n    Body: {}\n  Response: null",
                    requestEntry.getMethod(), requestBody
            );
        } else {
            log.info("Got response for a request.\n  Request:\n    Method: {}\n    Body: {}\n  Response: {}\n    Body: {}",
                    requestEntry.method, requestBody, response, response.body().replaceAll(ConfigUtils.UNIVERSAL_NEW_LINE_REGEXP, "")
            );
        }
    }
}
