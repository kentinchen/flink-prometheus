package com.flink.connectors.pushgateway.sink.pushgateway;

import org.apache.flink.api.connector.sink2.SinkWriter;
import org.apache.flink.api.connector.sink2.WriterInitContext;
import org.apache.flink.connector.base.sink.AsyncSinkBase;
import org.apache.flink.connector.base.sink.writer.BufferedRequestState;
import org.apache.flink.connector.base.sink.writer.ElementConverter;
import org.apache.flink.core.io.SimpleVersionedSerializer;

import java.io.IOException;
import java.util.Collection;
import java.util.Properties;

public class PushgatewaySink<InputT> extends AsyncSinkBase<InputT, PushgatewayGaugeEntity> {
    private String pushgateway;
    private final Properties properties;
    private ElementConverter<InputT, PushgatewayGaugeEntity> elementConverter;

   public PushgatewaySink(int maxBatchSize,
                          int maxInFlightRequests,
                          int maxBufferedRequests,
                          long maxBatchSizeInBytes,
                          long maxTimeInBufferMS,
                          long maxRecordSizeInBytes,
                          String pushgateway,Properties properties,ElementConverter<InputT, PushgatewayGaugeEntity> elementConverter){
       super(               elementConverter,
               maxBatchSize,
               maxInFlightRequests,
               maxBufferedRequests,
               maxBatchSizeInBytes,
               maxTimeInBufferMS,
               maxRecordSizeInBytes
       );
       this.pushgateway = pushgateway;
       this.properties = properties;
       this.elementConverter = elementConverter;
   }

    @Override
    public SinkWriter<InputT> createWriter(InitContext initContext) throws IOException {
        return null;
    }

    @Override
    public SinkWriter<InputT> createWriter(WriterInitContext context) throws IOException {
        return super.createWriter(context);
    }

    @Override
    public SimpleVersionedSerializer<BufferedRequestState<PushgatewayGaugeEntity>> getWriterStateSerializer() {
        return null;
    }

    @Override
    public StatefulSinkWriter<InputT, BufferedRequestState<PushgatewayGaugeEntity>> restoreWriter(InitContext context, Collection<BufferedRequestState<PushgatewayGaugeEntity>> recoveredState) throws IOException {
        return super.restoreWriter(context, recoveredState);
    }

    @Override
    public StatefulSinkWriter<InputT, BufferedRequestState<PushgatewayGaugeEntity>> restoreWriter(WriterInitContext context, Collection<BufferedRequestState<PushgatewayGaugeEntity>> recoveredState) throws IOException {
        return super.restoreWriter(context, recoveredState);
    }

    public static <InputT> PushgatewaySinkBuilder<InputT> builder() {
        return new PushgatewaySinkBuilder<>();
    }
}
