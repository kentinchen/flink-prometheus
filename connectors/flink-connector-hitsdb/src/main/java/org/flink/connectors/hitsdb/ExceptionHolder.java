package org.flink.connectors.hitsdb;

public interface ExceptionHolder {
    Exception getExceptionRef();

    void setExceptionRef(Exception paramException);
}
