package com.flink.connectors.pushgateway.sink.httpclient;

import com.flink.connectors.pushgateway.sink.HttpSinkRequestEntry;
import lombok.Data;
import lombok.NonNull;

import java.net.http.HttpResponse;
import java.util.Optional;

/**
 * A wrapper structure around an HTTP response, keeping a reference to a particular {@link
 * HttpSinkRequestEntry}. Used internally by the {@code HttpSinkWriter} to pass {@code
 * HttpSinkRequestEntry} along some other element that it is logically connected with.
 */
@Data
public final class JavaNetHttpResponseWrapper {
    @NonNull
    private final HttpRequest httpRequest;

    private final HttpResponse<String> response;

    public Optional<HttpResponse<String>> getResponse() {
        return Optional.ofNullable(response);
    }
}
