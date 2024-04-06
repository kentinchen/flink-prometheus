package com.flink.connectors.pushgateway.table.pushgateway;

import com.flink.connectors.pushgateway.sink.httpclient.HttpRequest;
import com.flink.connectors.pushgateway.sink.pushgateway.PushgatewayGaugeEntity;
import com.flink.connectors.pushgateway.sink.pushgateway.PushgatewaySink;
import com.flink.connectors.pushgateway.sink.pushgateway.PushgatewaySinkBuilder;
import com.flink.connectors.pushgateway.table.callback.HttpPostRequestCallback;
import org.apache.flink.api.common.serialization.SerializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.configuration.ReadableConfig;
import org.apache.flink.connector.base.table.sink.AsyncDynamicTableSink;
import org.apache.flink.connector.base.table.sink.AsyncDynamicTableSinkBuilder;
import org.apache.flink.table.connector.ChangelogMode;
import org.apache.flink.table.connector.format.EncodingFormat;
import org.apache.flink.table.connector.sink.DynamicTableSink;
import org.apache.flink.table.connector.sink.SinkV2Provider;
import org.apache.flink.table.data.RowData;
import org.apache.flink.table.types.DataType;
import org.apache.flink.types.RowKind;
import org.apache.flink.util.Preconditions;

import javax.annotation.Nullable;
import java.util.Properties;

import static com.flink.connectors.pushgateway.table.pushgateway.PushgatewyaDynamicSinkConnectorOptions.INSERT_METHOD;
import static com.flink.connectors.pushgateway.table.pushgateway.PushgatewyaDynamicSinkConnectorOptions.PUSHGATEWAY;

public class PushgatewayDynamicSink extends AsyncDynamicTableSink<PushgatewayGaugeEntity> {
    private final DataType consumedDataType;
    private final EncodingFormat<SerializationSchema<RowData>> encodingFormat;
    private final HttpPostRequestCallback<HttpRequest> httpPostRequestCallback;
    private final ReadableConfig tableOptions;
    private final Properties properties;

    protected PushgatewayDynamicSink(@Nullable Integer maxBatchSize, @Nullable Integer maxInFlightRequests,
                                     @Nullable Integer maxBufferedRequests, @Nullable Long maxBufferSizeInBytes,
                                     @Nullable Long maxTimeInBufferMS, DataType consumedDataType,
                                     EncodingFormat<SerializationSchema<RowData>> encodingFormat,
                                     HttpPostRequestCallback<HttpRequest> httpPostRequestCallback,
                                     ReadableConfig tableOptions,
                                     Properties properties) {
        super(maxBatchSize, maxInFlightRequests, maxBufferedRequests, maxBufferSizeInBytes, maxTimeInBufferMS);
        this.consumedDataType = Preconditions.checkNotNull(consumedDataType, "Consumed data type must not be null");
        this.encodingFormat = Preconditions.checkNotNull(encodingFormat, "Encoding format must not be null");
        this.httpPostRequestCallback = Preconditions.checkNotNull(httpPostRequestCallback, "Post request callback must not be null");
        this.tableOptions = Preconditions.checkNotNull(tableOptions, "Table options must not be null");
        this.properties = properties;
    }

    @Override
    public ChangelogMode getChangelogMode(ChangelogMode changelogMode) {
        // return encodingFormat.getChangelogMode();
        return ChangelogMode.newBuilder()
                .addContainedKind(RowKind.INSERT)
                .addContainedKind(RowKind.DELETE)
                .build();
    }

    @Override
    public SinkRuntimeProvider getSinkRuntimeProvider(Context context) {
        SerializationSchema<RowData> serializationSchema = encodingFormat.createRuntimeEncoder(context, consumedDataType);
        final TypeInformation<RowData> rowDataTypeInfo = context.createTypeInformation(consumedDataType);
        PushgatewaySinkBuilder<RowData> builder = PushgatewaySink
                .<RowData>builder()
                .setEndpointUrl(tableOptions.get(PUSHGATEWAY))
                .setElementConverter(PushgatewayConverterFactory.create(rowDataTypeInfo, consumedDataType, serializationSchema))
                .setProperties(properties);
        addAsyncOptionsToSinkBuilder(builder);
        return SinkV2Provider.of(builder.build());
    }

    @Override
    public DynamicTableSink copy() {
        return new PushgatewayDynamicSink(
                maxBatchSize,
                maxInFlightRequests,
                maxBufferedRequests,
                maxBufferSizeInBytes,
                maxTimeInBufferMS,
                consumedDataType,
                encodingFormat,
                httpPostRequestCallback,
                tableOptions,
                properties
        );
    }

    @Override
    public String asSummaryString() {
        return "PushgatewayDynamicSink";
    }

    public static class PushgatewayDynamicTableSinkBuilder extends AsyncDynamicTableSinkBuilder<PushgatewayGaugeEntity, PushgatewayDynamicTableSinkBuilder> {
        private final Properties properties = new Properties();
        private ReadableConfig tableOptions;
        private DataType consumedDataType;
        private EncodingFormat<SerializationSchema<RowData>> encodingFormat;
        private HttpPostRequestCallback<HttpRequest> httpPostRequestCallback;

        public PushgatewayDynamicTableSinkBuilder setTableOptions(ReadableConfig tableOptions) {
            this.tableOptions = tableOptions;
            return this;
        }

        public PushgatewayDynamicTableSinkBuilder setEncodingFormat(EncodingFormat<SerializationSchema<RowData>> encodingFormat) {
            this.encodingFormat = encodingFormat;
            return this;
        }

        public PushgatewayDynamicTableSinkBuilder setHttpPostRequestCallback(HttpPostRequestCallback<HttpRequest> httpPostRequestCallback) {
            this.httpPostRequestCallback = httpPostRequestCallback;
            return this;
        }

        public PushgatewayDynamicTableSinkBuilder setConsumedDataType(DataType consumedDataType) {
            this.consumedDataType = consumedDataType;
            return this;
        }

        public PushgatewayDynamicTableSinkBuilder setProperty(String propertyName, String propertyValue) {
            this.properties.setProperty(propertyName, propertyValue);
            return this;
        }

        public PushgatewayDynamicTableSinkBuilder setProperties(Properties properties) {
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
                    getMaxTimeInBufferMS(),
                    consumedDataType,
                    encodingFormat,
                    httpPostRequestCallback,
                    tableOptions,
                    properties
            );
        }
    }
}
