package com.flink.connectors.pushgateway.sink.pushgateway;

import org.apache.flink.connector.base.sink.writer.AsyncSinkWriterStateSerializer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class PushgatewaySinkWriterStateSerializer extends AsyncSinkWriterStateSerializer<ArrayList<PushgatewayGaugeEntity>> {
    @Override
    protected void serializeRequestToStream(ArrayList<PushgatewayGaugeEntity> list, DataOutputStream out) throws IOException {
        for (PushgatewayGaugeEntity s : list) {
            out.writeUTF(s.jobName);
            out.writeUTF(String.valueOf(s.timestamp));
            out.writeUTF(s.metricName);
            out.writeUTF(s.metricHelp);
            out.writeUTF(String.valueOf(s.metricValue));
            out.writeUTF(String.valueOf(s.labelKey));
        }
    }

    @Override
    protected ArrayList<PushgatewayGaugeEntity> deserializeRequestFromStream(long l, DataInputStream in) throws IOException {
        String jobName = in.readUTF();
        String timestamp = in.readUTF();
        String metricName = in.readUTF();
        String metricHelp = in.readUTF();
        double metricValue = Double.parseDouble(in.readUTF());
        PushgatewayGaugeEntity elm = new PushgatewayGaugeEntity(jobName, Long.valueOf(timestamp), metricName, metricHelp, metricValue, null);
        ArrayList<PushgatewayGaugeEntity> result = new ArrayList<>();
        result.add(elm);
        return result;
    }

    @Override
    public int getVersion() {
        return 1;
    }
}
