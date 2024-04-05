package com.flink.connectors.pushgateway.table.callback;

import java.io.Serializable;
import java.net.http.HttpResponse;
import java.util.Map;

public interface HttpPostRequestCallback<RequestT> extends Serializable {
    void call(
            HttpResponse<String> response,
            RequestT requestEntry,
            String endpointUrl,
            Map<String, String> headerMap
    );
}
