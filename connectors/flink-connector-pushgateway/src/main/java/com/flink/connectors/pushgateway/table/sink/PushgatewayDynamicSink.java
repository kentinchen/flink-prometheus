package com.flink.connectors.pushgateway.table.sink;

import com.flink.connectors.pushgateway.table.sink.callback.HttpPostRequestCallback;
import org.apache.flink.api.common.serialization.SerializationSchema;
import org.apache.flink.configuration.ReadableConfig;
import org.apache.flink.connector.base.table.sink.AsyncDynamicTableSink;
import org.apache.flink.connector.base.table.sink.AsyncDynamicTableSinkBuilder;
import org.apache.flink.table.connector.ChangelogMode;
import org.apache.flink.table.connector.format.EncodingFormat;
import org.apache.flink.table.connector.sink.DynamicTableSink;
import org.apache.flink.table.data.RowData;
import org.apache.flink.table.types.DataType;

import javax.annotation.Nullable;
import java.util.Properties;

public class PushgatewayDynamicSink extends AsyncDynamicTableSink<HttpSinkRequestEntry> {

    protected PushgatewayDynamicSink(@Nullable Integer maxBatchSize, @Nullable Integer maxInFlightRequests,
                                     @Nullable Integer maxBufferedRequests, @Nullable Long maxBufferSizeInBytes,
                                     @Nullable Long maxTimeInBufferMS) {
        super(maxBatchSize, maxInFlightRequests, maxBufferedRequests, maxBufferSizeInBytes, maxTimeInBufferMS);
    }

    @Override
    public ChangelogMode getChangelogMode(ChangelogMode changelogMode) {
        return null;
    }

    @Override
    public SinkRuntimeProvider getSinkRuntimeProvider(Context context) {
        return null;
    }

    @Override
    public DynamicTableSink copy() {
        return null;
    }

    @Override
    public String asSummaryString() {
        return "";
    }

    public static class HttpDynamicTableSinkBuilder
            extends AsyncDynamicTableSinkBuilder<HttpSinkRequestEntry, HttpDynamicTableSinkBuilder> {

        private final Properties properties = new Properties();

        private ReadableConfig tableOptions;

        private DataType consumedDataType;

        private EncodingFormat<SerializationSchema<RowData>> encodingFormat;

        private HttpPostRequestCallback<HttpRequest> httpPostRequestCallback;

        /**
         * @param tableOptions the {@link ReadableConfig} consisting of options listed in table
         *                     creation DDL
         * @return {@link HttpDynamicTableSinkBuilder} itself
         */
        public HttpDynamicTableSinkBuilder setTableOptions(ReadableConfig tableOptions) {
            this.tableOptions = tableOptions;
            return this;
        }

        /**
         * @param encodingFormat the format for encoding records
         * @return {@link HttpDynamicTableSinkBuilder} itself
         */
        public HttpDynamicTableSinkBuilder setEncodingFormat(
                EncodingFormat<SerializationSchema<RowData>> encodingFormat) {
            this.encodingFormat = encodingFormat;
            return this;
        }

        /**
         * @param httpPostRequestCallback the {@link HttpPostRequestCallback} implementation
         *                                for processing of requests and responses
         * @return {@link HttpDynamicTableSinkBuilder} itself
         */
        public HttpDynamicTableSinkBuilder setHttpPostRequestCallback(
                HttpPostRequestCallback<HttpRequest> httpPostRequestCallback) {
            this.httpPostRequestCallback = httpPostRequestCallback;
            return this;
        }

        /**
         * @param consumedDataType the consumed data type of the table
         * @return {@link HttpDynamicTableSinkBuilder} itself
         */
        public HttpDynamicTableSinkBuilder setConsumedDataType(DataType consumedDataType) {
            this.consumedDataType = consumedDataType;
            return this;
        }

        /**
         * Set property for Http Sink.
         * @param propertyName property name
         * @param propertyValue property value
         * @return {@link HttpDynamicTableSinkBuilder} itself
         */
        public HttpDynamicTableSinkBuilder setProperty(String propertyName, String propertyValue) {
            this.properties.setProperty(propertyName, propertyValue);
            return this;
        }

        /**
         * Add properties to Http Sink configuration
         * @param properties properties to add
         * @return {@link HttpDynamicTableSinkBuilder} itself
         */
        public HttpDynamicTableSinkBuilder setProperties(Properties properties) {
            this.properties.putAll(properties);
            return this;
        }

        @Override
        public PushgatewayDynamicSink build() {
            return new PushgatewayDynamicSink(
                    getMaxBatchSize(),
                    getMaxInFlightRequests(),
                    getMaxBufferedRequests(),
                    getMaxBufferSizeInBytes(),
                    getMaxTimeInBufferMS()
            );
        }
    }
}
