package com.flink.connectors.pushgateway.table.sink;

import lombok.Data;

import java.util.List;

@Data
public class HttpRequest {

    public final java.net.http.HttpRequest httpRequest;

    public final List<byte[]> elements;

    public final String method;

}
