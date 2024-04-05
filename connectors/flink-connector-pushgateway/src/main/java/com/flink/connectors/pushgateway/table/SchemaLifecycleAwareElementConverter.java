package com.flink.connectors.pushgateway.table;

import org.apache.flink.api.connector.sink2.Sink.InitContext;
import org.apache.flink.connector.base.sink.writer.ElementConverter;

public interface SchemaLifecycleAwareElementConverter<InputT, RequestEntryT>
        extends ElementConverter<InputT, RequestEntryT> {
    void open(InitContext context);
}
