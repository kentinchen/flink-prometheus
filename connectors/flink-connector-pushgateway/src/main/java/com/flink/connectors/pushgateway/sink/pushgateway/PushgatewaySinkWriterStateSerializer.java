package com.flink.connectors.pushgateway.sink.pushgateway;

import com.flink.connectors.pushgateway.sink.http.HttpSinkRequestEntry;
import org.apache.flink.connector.base.sink.writer.AsyncSinkWriterStateSerializer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PushgatewaySinkWriterStateSerializer  extends AsyncSinkWriterStateSerializer<HttpSinkRequestEntry> {
    @Override
    protected void serializeRequestToStream(HttpSinkRequestEntry httpSinkRequestEntry, DataOutputStream dataOutputStream) throws IOException {

    }

    @Override
    protected HttpSinkRequestEntry deserializeRequestFromStream(long l, DataInputStream dataInputStream) throws IOException {
        return null;
    }

    @Override
    public int getVersion() {
        return 0;
    }
}
