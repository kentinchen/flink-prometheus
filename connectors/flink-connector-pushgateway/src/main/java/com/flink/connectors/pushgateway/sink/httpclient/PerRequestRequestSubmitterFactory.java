package com.flink.connectors.pushgateway.sink.httpclient;

import com.flink.connectors.pushgateway.sink.http.RequestSubmitter;
import com.flink.connectors.pushgateway.sink.http.RequestSubmitterFactory;
import com.flink.connectors.pushgateway.utils.ThreadUtils;
import org.apache.flink.util.concurrent.ExecutorThreadFactory;

import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PerRequestRequestSubmitterFactory implements RequestSubmitterFactory {
    int HTTP_CLIENT_THREAD_POOL_SIZE = 1;

    @Override
    public RequestSubmitter createSubmitter(Properties properties, String[] headersAndValues) {
        ExecutorService httpClientExecutor = Executors.newFixedThreadPool(HTTP_CLIENT_THREAD_POOL_SIZE,
                new ExecutorThreadFactory("http-sink-client-per-request-worker", ThreadUtils.LOGGING_EXCEPTION_HANDLER));
        return new PerRequestSubmitter(properties, headersAndValues, null
                //JavaNetHttpClientFactory.createClient(properties, httpClientExecutor)
        );
    }
}
