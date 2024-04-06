package com.flink.connectors.pushgateway.sink.pushgateway;

import com.flink.connectors.pushgateway.table.SchemaLifecycleAwareElementConverter;
import org.apache.flink.annotation.PublicEvolving;
import org.apache.flink.connector.base.sink.AsyncSinkBase;
import org.apache.flink.connector.base.sink.AsyncSinkBaseBuilder;
import org.apache.flink.connector.base.sink.writer.ElementConverter;

import java.util.Optional;
import java.util.Properties;

public class PushgatewaySinkBuilder<InputT> extends AsyncSinkBaseBuilder<InputT, PushgatewayGaugeEntity, PushgatewaySinkBuilder<InputT>> {
    private static final int DEFAULT_MAX_BATCH_SIZE = 500;
    private static final int DEFAULT_MAX_IN_FLIGHT_REQUESTS = 50;
    private static final int DEFAULT_MAX_BUFFERED_REQUESTS = 10_000;
    private static final long DEFAULT_MAX_BATCH_SIZE_IN_B = 5 * 1024 * 1024;
    private static final long DEFAULT_MAX_TIME_IN_BUFFER_MS = 5000;
    private static final long DEFAULT_MAX_RECORD_SIZE_IN_B = 1024 * 1024;
    private final Properties properties = new Properties();
    // Mandatory field
    private String pushgateway;
    private ElementConverter<InputT, PushgatewayGaugeEntity> elementConverter;

    @PublicEvolving
    public PushgatewaySinkBuilder<InputT> setElementConverter(SchemaLifecycleAwareElementConverter<InputT, PushgatewayGaugeEntity> elementConverter) {
        this.elementConverter = elementConverter;
        return this;
    }

    public PushgatewaySinkBuilder<InputT> setEndpointUrl(String pushgateway) {
        this.pushgateway = pushgateway;
        return this;
    }

    public PushgatewaySinkBuilder<InputT> setProperties(Properties properties) {
        this.properties.putAll(properties);
        return this;
    }

    @Override
    public AsyncSinkBase<InputT, PushgatewayGaugeEntity> build() {
        return new PushgatewaySink<>(
                Optional.ofNullable(getMaxBatchSize()).orElse(DEFAULT_MAX_BATCH_SIZE),
                Optional.ofNullable(getMaxInFlightRequests()).orElse(DEFAULT_MAX_IN_FLIGHT_REQUESTS),
                Optional.ofNullable(getMaxBufferedRequests()).orElse(DEFAULT_MAX_BUFFERED_REQUESTS),
                Optional.ofNullable(getMaxBatchSizeInBytes()).orElse(DEFAULT_MAX_BATCH_SIZE_IN_B),
                Optional.ofNullable(getMaxTimeInBufferMS()).orElse(DEFAULT_MAX_TIME_IN_BUFFER_MS),
                Optional.ofNullable(getMaxRecordSizeInBytes()).orElse(DEFAULT_MAX_RECORD_SIZE_IN_B),
                pushgateway,
                properties,
                elementConverter
        );
    }
}
