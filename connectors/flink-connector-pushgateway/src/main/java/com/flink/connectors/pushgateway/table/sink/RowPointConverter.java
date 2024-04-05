package com.flink.connectors.pushgateway.table.sink;

import org.apache.flink.types.Row;

import java.io.Serializable;

public interface RowPointConverter extends Serializable {
    String convert(Row paramRow);
}
