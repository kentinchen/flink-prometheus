package com.flink.connectors.pushgateway.sink.pushgateway;

import org.apache.flink.connector.base.sink.writer.AsyncSinkWriterStateSerializer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PushgatewaySinkWriterStateSerializer extends AsyncSinkWriterStateSerializer<PushgatewayGaugeEntity> {
    @Override
    protected void serializeRequestToStream(PushgatewayGaugeEntity s, DataOutputStream out) throws IOException {
        out.writeUTF(s.jobName);
        out.writeUTF(String.valueOf(s.timestamp));
        out.writeUTF(s.metricName);
        out.writeUTF(s.metricHelp);
        out.writeUTF(String.valueOf(s.metricValue));
    }

    @Override
    protected PushgatewayGaugeEntity deserializeRequestFromStream(long l, DataInputStream in) throws IOException {
        var jobName = in.readUTF();
        var timestamp = in.readUTF();
        var metricName = in.readUTF();
        var metricHelp = in.readUTF();
        var metricValue = in.readUTF();
        return new PushgatewayGaugeEntity(jobName, Long.valueOf(timestamp), metricName, metricHelp, Double.valueOf(metricValue), null);
    }

    @Override
    public int getVersion() {
        return 1;
    }
}
