package com.flink.connectors.pushgateway.sink.function;

public interface ExceptionHolder {
    Exception getExceptionRef();

    void setExceptionRef(Exception paramException);
}
