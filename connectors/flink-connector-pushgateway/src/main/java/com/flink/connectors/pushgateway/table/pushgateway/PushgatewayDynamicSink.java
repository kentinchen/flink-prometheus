package com.flink.connectors.pushgateway.table.pushgateway;

import com.flink.connectors.pushgateway.sink.pushgateway.PushgatewayGaugeEntity;
import com.flink.connectors.pushgateway.sink.pushgateway.PushgatewaySink;
import com.flink.connectors.pushgateway.sink.pushgateway.PushgatewaySinkBuilder;
import org.apache.flink.api.common.serialization.SerializationSchema;
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

import static com.flink.connectors.pushgateway.table.pushgateway.PushgatewayDynamicSinkConnectorOptions.PUSHGATEWAY;

public class PushgatewayDynamicSink extends AsyncDynamicTableSink<PushgatewayGaugeEntity> {
    private final DataType consumedDataType;
    private final EncodingFormat<SerializationSchema<RowData>> encodingFormat;
    private final ReadableConfig tableOptions;

    protected PushgatewayDynamicSink(@Nullable Integer maxBatchSize, @Nullable Integer maxInFlightRequests,
                                     @Nullable Integer maxBufferedRequests, @Nullable Long maxBufferSizeInBytes,
                                     @Nullable Long maxTimeInBufferMS, DataType consumedDataType,
                                     EncodingFormat<SerializationSchema<RowData>> encodingFormat,
                                     ReadableConfig tableOptions) {
        super(maxBatchSize, maxInFlightRequests, maxBufferedRequests, maxBufferSizeInBytes, maxTimeInBufferMS);
        this.consumedDataType = Preconditions.checkNotNull(consumedDataType, "Consumed data type must not be null");
        this.encodingFormat = Preconditions.checkNotNull(encodingFormat, "Encoding format must not be null");
        this.tableOptions = Preconditions.checkNotNull(tableOptions, "Table options must not be null");
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
        PushgatewaySinkBuilder<RowData> builder = PushgatewaySink
                .<RowData>builder()
                .setEndpointUrl(tableOptions.get(PUSHGATEWAY))
                .setElementConverter(PushgatewayConverterFactory.create(tableOptions,consumedDataType,serializationSchema));
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
                tableOptions
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

        public PushgatewayDynamicTableSinkBuilder setTableOptions(ReadableConfig tableOptions) {
            this.tableOptions = tableOptions;
            return this;
        }

        public PushgatewayDynamicTableSinkBuilder setEncodingFormat(EncodingFormat<SerializationSchema<RowData>> encodingFormat) {
            this.encodingFormat = encodingFormat;
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
                    tableOptions
            );
        }
    }
}
