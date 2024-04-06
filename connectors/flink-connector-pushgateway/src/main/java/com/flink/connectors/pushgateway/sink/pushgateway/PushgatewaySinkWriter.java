package com.flink.connectors.pushgateway.sink.pushgateway;

import org.apache.flink.api.common.eventtime.Watermark;
import org.apache.flink.api.connector.sink2.WriterInitContext;
import org.apache.flink.connector.base.sink.writer.AsyncSinkWriter;
import org.apache.flink.connector.base.sink.writer.BufferedRequestState;
import org.apache.flink.connector.base.sink.writer.ElementConverter;
import org.apache.flink.connector.base.sink.writer.config.AsyncSinkWriterConfiguration;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

public class PushgatewaySinkWriter <InputT> extends AsyncSinkWriter<InputT, PushgatewayGaugeEntity> {
    public PushgatewaySinkWriter(ElementConverter<InputT, PushgatewayGaugeEntity> elementConverter,
                                 WriterInitContext context, AsyncSinkWriterConfiguration configuration,
                                 Collection<BufferedRequestState<PushgatewayGaugeEntity>> bufferedRequestStates) {
        super(elementConverter, context, configuration, bufferedRequestStates);
    }

    @Override
    protected void submitRequestEntries(List<PushgatewayGaugeEntity> list, Consumer<List<PushgatewayGaugeEntity>> consumer) {

    }

    @Override
    protected long getSizeInBytes(PushgatewayGaugeEntity pushgatewayGaugeEntity) {
        return 0;
    }

    @Override
    public void writeWatermark(Watermark watermark) throws IOException, InterruptedException {
        super.writeWatermark(watermark);
    }
}
