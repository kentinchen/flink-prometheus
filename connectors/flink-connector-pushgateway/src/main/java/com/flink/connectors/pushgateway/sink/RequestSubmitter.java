package com.flink.connectors.pushgateway.sink;

import com.flink.connectors.pushgateway.sink.httpclient.JavaNetHttpResponseWrapper;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Submits request via HTTP.
 */
public interface RequestSubmitter {
    List<CompletableFuture<JavaNetHttpResponseWrapper>> submit(String endpointUrl, List<HttpSinkRequestEntry> requestToSubmit);
}
