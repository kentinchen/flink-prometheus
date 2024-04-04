package com.flink.connectors.pushgateway.config;

/**
 * A Runtime exception throw when there is any issue with configuration properties for Http
 * Connector.
 */
public class NotEnoughParamsException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public NotEnoughParamsException(String message) {
        super(message);
    }

    public NotEnoughParamsException(String message, Throwable t) {
        super(message, t);
    }

    /**
     * Creates an exception object using predefined exception message template:
     * {@code Invalid value + (value) + for configuration + (property name) + (additional message) }
     * @param name configuration property name.
     * @param value configuration property value.
     * @param message custom message appended to the end of exception message.
     */
    public NotEnoughParamsException(String name, Object value, String message) {
        super("Invalid value " + value + " for configuration " + name + (message == null ? ""
            : ": " + message));
    }
}
