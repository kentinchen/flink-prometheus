package com.flink.connectors.pushgateway.utils;

public class ExcpUtil {
    //打印异常堆栈信息
    public static String getStackTraceString(Throwable ex) {
        StackTraceElement[] traceElements = ex.getStackTrace();
        StringBuilder traceBuilder = new StringBuilder();
        if (traceElements != null) {
            for (StackTraceElement traceElement : traceElements) {
                traceBuilder.append(traceElement.toString());
                traceBuilder.append("\n");
            }
        }
        return traceBuilder.toString();
    }

    //构造异常堆栈信息
    public static String buildErrorMessage(Exception ex) {
        String result;
        String stackTrace = getStackTraceString(ex);
        String exceptionType = ex.toString();
        String exceptionMessage = ex.getMessage();
        result = String.format("%s : %s \r\n %s", exceptionType, exceptionMessage, stackTrace);
        return result;
    }
}