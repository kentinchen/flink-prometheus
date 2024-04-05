package com.flink.connectors.pushgateway.sink.httpclient;

import com.flink.connectors.pushgateway.config.ConfigException;
import com.flink.connectors.pushgateway.config.ConnectorConfigConstants;
import com.flink.connectors.pushgateway.sink.RequestSubmitterFactory;
import com.flink.connectors.pushgateway.utils.ThreadUtils;
import org.apache.flink.util.StringUtils;
import org.apache.flink.util.concurrent.ExecutorThreadFactory;

import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BatchRequestSubmitterFactory implements RequestSubmitterFactory {
    private final String maxBatchSize;
    int HTTP_CLIENT_THREAD_POOL_SIZE = 1;

    public BatchRequestSubmitterFactory(int maxBatchSize) {
        if (maxBatchSize < 1) {
            throw new IllegalArgumentException("Batch Request submitter batch size must be greater than zero.");
        }
        this.maxBatchSize = String.valueOf(maxBatchSize);
    }

    @Override
    public BatchRequestSubmitter createSubmitter(Properties properties, String[] headersAndValues) {
        String batchRequestSize = properties.getProperty(ConnectorConfigConstants.SINK_HTTP_BATCH_REQUEST_SIZE);
        if (StringUtils.isNullOrWhitespaceOnly(batchRequestSize)) {
            properties.setProperty(ConnectorConfigConstants.SINK_HTTP_BATCH_REQUEST_SIZE, maxBatchSize);
        } else {
            try {
                int batchSize = Integer.parseInt(batchRequestSize);
                if (batchSize < 1) {
                    throw new ConfigException(String.format("Property %s must be greater than 0 but was: %s",
                            ConnectorConfigConstants.SINK_HTTP_BATCH_REQUEST_SIZE, batchRequestSize));
                }
            } catch (NumberFormatException e) {
                throw new ConfigException(String.format("Property %s must be an integer but was: %s",
                        ConnectorConfigConstants.SINK_HTTP_BATCH_REQUEST_SIZE, batchRequestSize), e);
            }
        }
        ExecutorService httpClientExecutor = Executors.newFixedThreadPool(HTTP_CLIENT_THREAD_POOL_SIZE,
                new ExecutorThreadFactory("http-sink-client-batch-request-worker", ThreadUtils.LOGGING_EXCEPTION_HANDLER));
        return new BatchRequestSubmitter(properties, headersAndValues, null
                //JavaNetHttpClientFactory.createClient(properties, httpClientExecutor)
        );
    }
}
