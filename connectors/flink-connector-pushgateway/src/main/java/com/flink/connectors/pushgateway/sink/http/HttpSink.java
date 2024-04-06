package com.flink.connectors.pushgateway.sink.http;

import com.flink.connectors.pushgateway.sink.httpclient.HttpRequest;
import com.flink.connectors.pushgateway.sink.internal.HttpSinkInternal;
import com.flink.connectors.pushgateway.table.callback.HttpPostRequestCallback;
import org.apache.flink.annotation.PublicEvolving;
import org.apache.flink.connector.base.sink.writer.ElementConverter;

import java.util.Properties;

/**
 * A public implementation for {@code HttpSink} that performs async requests against a specified
 * HTTP endpoint using the buffering protocol specified in
 * {@link org.apache.flink.connector.base.sink.AsyncSinkBase}.
 *
 * <p>
 * To create a new instance  of this class use {@link HttpSinkBuilder}. An example would be:
 * <pre>{@code
 * HttpSink<String> httpSink =
 *     HttpSink.<String>builder()
 *             .setEndpointUrl("http://example.com/myendpoint")
 *             .setElementConverter(
 *                 (s, _context) -> new HttpSinkRequestEntry("POST", "text/plain",
 *                 s.getBytes(StandardCharsets.UTF_8)))
 *             .build();
 * }</pre>
 *
 * @param <InputT> type of the elements that should be sent through HTTP request.
 */
@PublicEvolving
public class HttpSink<InputT> extends HttpSinkInternal<InputT> {
    HttpSink(
            ElementConverter<InputT, HttpSinkRequestEntry> elementConverter,
            int maxBatchSize,
            int maxInFlightRequests,
            int maxBufferedRequests,
            long maxBatchSizeInBytes,
            long maxTimeInBufferMS,
            long maxRecordSizeInBytes,
            String endpointUrl,
            HttpPostRequestCallback<HttpRequest> httpPostRequestCallback,
            Properties properties) {
        super(elementConverter,
                maxBatchSize,
                maxInFlightRequests,
                maxBufferedRequests,
                maxBatchSizeInBytes,
                maxTimeInBufferMS,
                maxRecordSizeInBytes,
                endpointUrl,
                httpPostRequestCallback,
                properties
        );
    }

    /**
     * Create a {@link HttpSinkBuilder} constructing a new {@link HttpSink}.
     *
     * @param <InputT> type of the elements that should be sent through HTTP request
     * @return {@link HttpSinkBuilder}
     */
    public static <InputT> HttpSinkBuilder<InputT> builder() {
        return new HttpSinkBuilder<>();
    }
}
