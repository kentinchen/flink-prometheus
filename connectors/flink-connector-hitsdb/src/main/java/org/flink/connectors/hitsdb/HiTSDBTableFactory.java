package org.flink.connectors.hitsdb;

import com.alibaba.blink.connectors.hitsdb.sink.HiTSDBTableSink;
import com.alibaba.blink.streaming.connectors.common.exception.NotEnoughParamsException;
import com.alibaba.blink.streaming.connectors.common.util.BlinkStringUtil;
import com.alibaba.blink.table.connectors.conf.BlinkOptions;
import com.alibaba.blink.table.factories.BlinkTableFactory;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.table.api.RichTableSchema;
import org.apache.flink.table.factories.BatchCompatibleTableSinkFactory;
import org.apache.flink.table.factories.StreamTableSinkFactory;
import org.apache.flink.table.sinks.BatchCompatibleStreamTableSink;
import org.apache.flink.table.sinks.StreamTableSink;
import org.apache.flink.table.util.TableProperties;
import org.apache.flink.types.Row;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HiTSDBTableFactory extends BlinkTableFactory implements StreamTableSinkFactory<Tuple2<Boolean, Row>>, BatchCompatibleTableSinkFactory<Tuple2<Boolean, Row>> {
    private HiTSDBTableSink createSink(Map<String, String> props) {
        HiTSDBOutputFormat.Builder outputFormatBuilder;
        TableProperties properties = new TableProperties();
        properties.putProperties(props);
        RichTableSchema schema = properties.readSchemaFromProperties(this.classLoader);
        int batchPutBufferSize = properties.getInteger(BlinkOptions.HiTSDB.BATCH_PUT_BUFFER_SIZE);
        int multiFieldBatchPutBufferSize = properties.getInteger(BlinkOptions.HiTSDB.MULTI_FIELD_BATCH_PUT_BUFFER_SIZE);
        int batchPutConsumerThreadCount = properties.getInteger(BlinkOptions.HiTSDB.BATCH_PUT_CONSUMER_THREAD_COUNT);
        int multiFieldBatchPutConsumerThreadCount = properties.getInteger(BlinkOptions.HiTSDB.MULTI_FIELD_BATCH_PUT_CONSUMER_THREAD_COUNT);
        int batchPutRetryCount = properties.getInteger(BlinkOptions.HiTSDB.BATCH_PUT_RETRY_COUNT);
        int batchPutSize = properties.getInteger(BlinkOptions.HiTSDB.BATCH_PUT_SIZE);
        int batchPutTimeLimit = properties.getInteger(BlinkOptions.HiTSDB.BATCH_PUT_TIME_LIMIT);
        boolean httpCompress = properties.getBoolean(BlinkOptions.HiTSDB.HTTP_COMPRESS);
        int httpConnectionPool = properties.getInteger(BlinkOptions.HiTSDB.HTTP_CONNECTION_POOL);
        int httpConnectTimeout = properties.getInteger(BlinkOptions.HiTSDB.HTTP_CONNECT_TIMEOUT);
        int ioThreadCount = properties.getInteger(BlinkOptions.HiTSDB.IO_THREAD_COUNT);
        boolean virtualDomainSwitch = properties.getBoolean(BlinkOptions.HiTSDB.VIRTUAL_DOMAIN_SWITCH);
        String partitionBy = properties.getString(BlinkOptions.PARTITION_BY);
        boolean shuffleEmptyKey = properties.getBoolean(BlinkOptions.SHUFFLE_EMPTY_KEY);
        boolean checkValid = properties.getBoolean(BlinkOptions.HiTSDB.CHECK_VALID);
        boolean ignoreWriteError = properties.getBoolean(HiTSDBOptions.IGNORE_WRITE_ERROR);
        boolean printPointDetails = properties.getBoolean(HiTSDBOptions.PRINT_POINT_DETAILS);
        boolean usePartition = properties.getBoolean(HiTSDBOptions.USE_PARTITION_MODE);
        String username = properties.getString(HiTSDBOptions.USERNAME);
        String password = properties.getString(HiTSDBOptions.PASSWORD);
        String database = properties.getString(HiTSDBOptions.DATABASE);
        if (usePartition) {
            HiTSDBShardedOutputFormat.Builder builder = new HiTSDBShardedOutputFormat.Builder();
            String partitionKey = properties.getString(HiTSDBOptions.PARTITION_KEY);
            if (BlinkStringUtil.isEmpty(partitionKey))
                throw new NotEnoughParamsException("partitionKey must not empty!");
            builder.setPartitionKey(partitionKey);
            String partitionHosts = properties.getString(HiTSDBOptions.PARTITION_HOSTS);
            if (BlinkStringUtil.isEmpty(partitionKey))
                throw new NotEnoughParamsException("partitionHosts must not empty!");
            List<Host> hosts = Host.parseHosts(partitionHosts);
            if (hosts.isEmpty())
                throw new NotEnoughParamsException("partitionHosts must formatted as 'host:port,host:port,host:port' ");
            builder.setPartitionHosts(hosts);
            outputFormatBuilder = builder;
        } else {
            outputFormatBuilder = new HiTSDBOutputFormat.Builder();
            String host = properties.getString(BlinkOptions.HiTSDB.HOST);
            if (BlinkStringUtil.isEmpty(host))
                throw new NotEnoughParamsException(BlinkOptions.HiTSDB.PARAMS_WRITER_HELP_MSG);
            outputFormatBuilder.setHost(host);
            int port = properties.getInteger(BlinkOptions.HiTSDB.PORT);
            outputFormatBuilder.setPort(port);
        }
        outputFormatBuilder.setBatchPutBufferSize(batchPutBufferSize)
                .setBatchPutConsumerThreadCount(batchPutConsumerThreadCount)
                .setMultiFieldBatchPutBufferSize(multiFieldBatchPutBufferSize)
                .setMultiFieldBatchPutConsumerThreadCount(multiFieldBatchPutConsumerThreadCount)
                .setBatchPutRetryCount(batchPutRetryCount)
                .setBatchPutSize(batchPutSize)
                .setBatchPutTimeLimit(batchPutTimeLimit)
                .setUsername(username)
                .setPassword(password)
                .setDatabase(database)
                .setHttpCompress(httpCompress)
                .setHttpConnectionPool(httpConnectionPool)
                .setHttpConnectTimeout(httpConnectTimeout)
                .setIoThreadCount(ioThreadCount)
                .setCheckValid(checkValid)
                .setIgnoreWriteError(ignoreWriteError)
                .setPrintPointDetails(printPointDetails)
                .setVirtualDomainSwitch(virtualDomainSwitch);
        HiTSDBTableSink sink = new HiTSDBTableSink(outputFormatBuilder, schema);
        if (partitionBy != null && !partitionBy.isEmpty()) {
            sink.setPartitionedField(partitionBy);
            sink.setShuffleEmptyKey(shuffleEmptyKey);
        }
        return sink;
    }

    protected List<String> supportedSpecificProperties() {
        return BlinkOptions.HiTSDB.SUPPORTED_KEYS;
    }

    protected Map<String, String> requiredContextSpecific() {
        Map<String, String> context = new HashMap<>();
        context.put("connector.type", "HITSDB");
        context.put("connector.property-version", "1");
        return context;
    }

    public BatchCompatibleStreamTableSink<Tuple2<Boolean, Row>> createBatchCompatibleTableSink(Map<String, String> properties) {
        return createSink(properties);
    }

    public StreamTableSink<Tuple2<Boolean, Row>> createStreamTableSink(Map<String, String> properties) {
        return createSink(properties);
    }
}
