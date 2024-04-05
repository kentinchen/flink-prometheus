package com.flink.connectors.pushgateway.config;


public class NotEnoughParamsException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public NotEnoughParamsException(String message) {
        super(message);
    }

    public NotEnoughParamsException(String message, Throwable t) {
        super(message, t);
    }

    public NotEnoughParamsException(String name, Object value, String message) {
        super("Invalid value " + value + " for configuration " + name + (message == null ? "" : ": " + message));
    }
}
