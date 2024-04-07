package org.flink.connectors.hitsdb;

import com.alibaba.blink.streaming.connectors.common.MetricUtils;
import com.aliyun.hitsdb.client.TSDB;
import com.aliyun.hitsdb.client.TSDBClientFactory;
import com.aliyun.hitsdb.client.TSDBConfig;
import com.aliyun.hitsdb.client.callback.AbstractBatchPutCallback;
import com.aliyun.hitsdb.client.callback.AbstractMultiFieldBatchPutCallback;
import com.aliyun.hitsdb.client.value.request.AbstractPoint;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.flink.api.java.typeutils.RowTypeInfo;
import org.apache.flink.types.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class HiTSDBShardedOutputFormat extends HiTSDBOutputFormat {
    public static final long SYNC_LOG_THRESHOLD = TimeUnit.SECONDS.toMillis(10L);
    private static final Logger LOG = LoggerFactory.getLogger(HiTSDBShardedOutputFormat.class);
    private static final long serialVersionUID = 7152024304257904657L;
    private final String partitionKey;
    private final List<Host> partitionHosts;
    private final int partitionNum;
    private final List<ClientHolder> tsdbClients = new ArrayList<>();

    public HiTSDBShardedOutputFormat(int batchPutBufferSize, AbstractBatchPutCallback<?> batchPutCallback, int batchPutConsumerThreadCount, int multiFieldBatchPutBufferSize, AbstractMultiFieldBatchPutCallback<?> multiFieldBatchPutCallback, int multiFieldBatchPutConsumerThreadCount, int batchPutRetryCount, int batchPutSize, int batchPutTimeLimit, String host, int port, String database, String username, String password, String partitionKey, List<Host> partitionHosts, boolean httpCompress, int httpConnectionPool, int httpConnectTimeout, int ioThreadCount, boolean virtualDomainSwitch, RowPointConverter rowConverter, RowTypeInfo rowTypeInfo, boolean ignoreWriteError, boolean printPointDetails) {
        super(batchPutBufferSize, batchPutCallback, batchPutConsumerThreadCount, multiFieldBatchPutBufferSize, multiFieldBatchPutCallback, multiFieldBatchPutConsumerThreadCount, batchPutRetryCount, batchPutSize, batchPutTimeLimit, host, port, database, username, password, httpCompress, httpConnectionPool, httpConnectTimeout, ioThreadCount, virtualDomainSwitch, rowConverter, rowTypeInfo, ignoreWriteError, printPointDetails);
        this.partitionKey = partitionKey;
        this.partitionHosts = partitionHosts;
        this.partitionNum = partitionHosts.size();
    }

    public void open(int taskNumber, int numTasks) throws IOException {
        superOpen(taskNumber, numTasks);
        synchronized (HiTSDBShardedOutputFormat.class) {
            for (Host host : this.partitionHosts)
                this.tsdbClients.add(getOrCreateClient(host.getName(), host.getPort()));
        }
        this.outTps = MetricUtils.registerOutTps(getRuntimeContext());
        this.outBps = MetricUtils.registerOutBps(getRuntimeContext(), "hitsdb");
        this.latencyGauge = MetricUtils.registerOutLatency(getRuntimeContext());
    }

    private ClientHolder getOrCreateClient(String host, int port) {
        TSDB tsdb;
        TSDBConfig.Builder hiTSDBConfigBuilder = TSDBConfig.address(host, port).batchPutBufferSize(this.batchPutBufferSize).batchPutConsumerThreadCount(this.batchPutConsumerThreadCount).listenBatchPut(this.batchPutCallback).multiFieldBatchPutBufferSize(this.multiFieldBatchPutBufferSize).listenMultiFieldBatchPut(this.multiFieldBatchPutCallback).multiFieldBatchPutConsumerThreadCount(this.multiFieldBatchPutConsumerThreadCount).batchPutRetryCount(this.batchPutRetryCount).batchPutSize(this.batchPutSize).batchPutTimeLimit(this.batchPutTimeLimit).httpCompress(this.httpCompress).httpConnectionPool(this.httpConnectionPool).httpConnectTimeout(this.httpConnectTimeout).ioThreadCount(this.ioThreadCount);
        Callback callback = new Callback(this, this.printPointDetails);
        if (!this.ignoreWriteError) {
            this.batchPutCallback = callback.batchPutCallback;
            this.multiFieldBatchPutCallback = callback.multiFieldBatchPutCallback;
            hiTSDBConfigBuilder.listenBatchPut(callback.batchPutCallback);
            hiTSDBConfigBuilder.listenMultiFieldBatchPut(callback.multiFieldBatchPutCallback);
        } else {
            this.batchPutCallback = callback.batchPutIgnoreErrorsCallback;
            this.multiFieldBatchPutCallback = callback.multiFieldBatchPutIgnoreErrorsCallback;
            hiTSDBConfigBuilder.listenBatchPut(callback.batchPutIgnoreErrorsCallback);
            hiTSDBConfigBuilder.listenMultiFieldBatchPut(callback.multiFieldBatchPutIgnoreErrorsCallback);
        }
        this.exceptionRef = new AtomicReference<>(null);
        TSDBConfig config = hiTSDBConfigBuilder.config();
        String configStr = toString(config);
        if (tsdbConnectionPool.contains(configStr)) {
            tsdb = tsdbConnectionPool.get(configStr);
        } else {
            tsdb = TSDBClientFactory.connect(config);
            tsdbConnectionPool.put(configStr, tsdb);
        }
        return new ClientHolder(configStr, tsdb);
    }

    public void close() throws IOException {
        synchronized (HiTSDBShardedOutputFormat.class) {
            for (ClientHolder holder : this.tsdbClients) {
                String connectionKey = holder.getConnectionKey();
                TSDB tsdb = holder.getTsdb();
                if (tsdbConnectionPool.remove(connectionKey) && tsdb != null)
                    tsdb.close();
            }
            checkExceptionRef();
        }
    }

    public void writeAddRecord(Row row) {
        if (null != row) {
            Map<String, String> tags;
            checkExceptionRef();
            long start = System.currentTimeMillis();
            AbstractPoint point = this.rowConverter.convert(row);
            tags = point.getTags();
            int idx = 0;
            if (tags == null || tags.isEmpty()) {
                idx = RandomUtils.nextInt(this.partitionNum);
            } else {
                String tagValue = tags.get(this.partitionKey);
                if (tagValue == null || tagValue.length() == 0) {
                    idx = RandomUtils.nextInt(this.partitionNum);
                } else {
                    int hashCode = murmurHash(tagValue) & Integer.MAX_VALUE;
                    idx = hashCode % this.partitionNum;
                }
            }
            TSDB targetTSDB = (this.tsdbClients.get(idx)).getTsdb();
            PointPutUtils.putPoint(targetTSDB, point, false, this.ignoreWriteError);
            long end = System.currentTimeMillis();
            this.outTps.markEvent();
            this.outBps.markEvent(1000L);
            this.latencyGauge.report(end - start);
        }
        checkExceptionRef();
    }

    private int murmurHash(String s) {
        int m = 1540483477;
        int r = 24;
        int offset = 0;
        int length = s.length();
        int h = length;
        int len4 = length >> 2;
        for (int i = 0; i < len4; i++) {
            int i4 = (i << 2) + offset;
            int k = s.charAt(i4 + 3);
            k <<= 8;
            k |= s.charAt(i4 + 2) & 0xFF;
            k <<= 8;
            k |= s.charAt(i4 + 1) & 0xFF;
            k <<= 8;
            k |= s.charAt(i4) & 0xFF;
            k *= m;
            k ^= k >>> r;
            k *= m;
            h *= m;
            h ^= k;
        }
        int lenM = len4 << 2;
        int left = length - lenM;
        int iM = lenM + offset;
        if (left != 0) {
            if (left >= 3)
                h ^= s.charAt(iM + 2) << 16;
            if (left >= 2)
                h ^= s.charAt(iM + 1) << 8;
            if (left >= 1)
                h ^= s.charAt(iM);
            h *= m;
        }
        h ^= h >>> 13;
        h *= m;
        h ^= h >>> 15;
        return h;
    }

    public void sync() {
        long start = System.currentTimeMillis();
        for (ClientHolder holder : this.tsdbClients)
            holder.getTsdb().flush();
        long elapsed = System.currentTimeMillis() - start;
        if (elapsed >= SYNC_LOG_THRESHOLD)
            LOG.info("sync {} clients points cost {} ms", elapsed);
    }

    public static class Builder extends HiTSDBOutputFormat.Builder {
        private String partitionKey;
        private List<Host> partitionHosts;

        public Builder setPartitionKey(String partitionKey) {
            this.partitionKey = partitionKey;
            return this;
        }

        public Builder setPartitionHosts(List<Host> partitionHosts) {
            this.partitionHosts = partitionHosts;
            return this;
        }

        public HiTSDBShardedOutputFormat build() {
            if (this.rowConverter == null)
                this.rowConverter = RowPointConverterFactory.create(this.rowTypeInfo, this.checkValid);
            return new HiTSDBShardedOutputFormat(this.batchPutBufferSize, this.batchPutCallback, this.batchPutConsumerThreadCount, this.multiFieldBatchPutBufferSize, this.multiFieldBatchPutCallback, this.multiFieldBatchPutConsumerThreadCount, this.batchPutRetryCount, this.batchPutSize, this.batchPutTimeLimit, this.host, this.port, this.database, this.username, this.password, this.partitionKey, this.partitionHosts, this.httpCompress, this.httpConnectionPool, this.httpConnectTimeout, this.ioThreadCount, this.virtualDomainSwitch, this.rowConverter, this.rowTypeInfo, this.ignoreWriteError, this.printPointDetails);
        }
    }

    private static class ClientHolder {
        private final String connectionKey;
        private final TSDB tsdb;

        public ClientHolder(String connectionKey, TSDB tsdb) {
            this.connectionKey = connectionKey;
            this.tsdb = tsdb;
        }

        public String getConnectionKey() {
            return this.connectionKey;
        }

        public TSDB getTsdb() {
            return this.tsdb;
        }
    }
}
