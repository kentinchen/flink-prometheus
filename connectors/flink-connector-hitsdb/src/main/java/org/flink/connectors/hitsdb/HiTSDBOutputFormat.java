package org.flink.connectors.hitsdb;

import com.alibaba.blink.streaming.connectors.common.MetricUtils;
import com.alibaba.blink.streaming.connectors.common.output.Syncable;
import com.alibaba.blink.streaming.connectors.common.output.TupleRichOutputFormat;
import com.alibaba.blink.streaming.connectors.common.util.ConnectionPool;
import com.aliyun.hitsdb.client.TSDB;
import com.aliyun.hitsdb.client.TSDBClientFactory;
import com.aliyun.hitsdb.client.TSDBConfig;
import com.aliyun.hitsdb.client.callback.AbstractBatchPutCallback;
import com.aliyun.hitsdb.client.callback.AbstractMultiFieldBatchPutCallback;
import com.aliyun.hitsdb.client.value.request.AbstractPoint;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.java.typeutils.RowTypeInfo;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.metrics.Meter;
import org.apache.flink.types.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

public class HiTSDBOutputFormat extends TupleRichOutputFormat implements Syncable, ExceptionHolder {
    protected static final ConnectionPool<TSDB> tsdbConnectionPool = new ConnectionPool<>();
    private static final long serialVersionUID = -1990329809578581052L;
    private static final Logger LOG = LoggerFactory.getLogger(HiTSDBOutputFormat.class);
    private final String host;
    private final int port;
    private final String database;
    private final String username;
    private final String password;
    protected int batchPutBufferSize;
    protected transient AbstractBatchPutCallback<?> batchPutCallback;
    protected int batchPutConsumerThreadCount;
    protected int multiFieldBatchPutBufferSize;
    protected transient AbstractMultiFieldBatchPutCallback<?> multiFieldBatchPutCallback;
    protected int multiFieldBatchPutConsumerThreadCount;
    protected int batchPutRetryCount;
    protected int batchPutSize;
    protected int batchPutTimeLimit;
    protected boolean httpCompress;
    protected int httpConnectionPool;
    protected int httpConnectTimeout;
    protected int ioThreadCount;
    protected RowPointConverter rowConverter;
    protected Meter outTps;
    protected Meter outBps;
    protected MetricUtils.LatencyGauge latencyGauge;
    protected boolean ignoreWriteError;
    protected boolean printPointDetails;
    protected transient AtomicReference<Exception> exceptionRef;
    private transient TSDB tsdb;
    private String configStr;
    private boolean virtualDomainSwitch;
    private RowTypeInfo rowTypeInfo;

    public HiTSDBOutputFormat(int batchPutBufferSize, AbstractBatchPutCallback<?> batchPutCallback, int batchPutConsumerThreadCount, int multiFieldBatchPutBufferSize, AbstractMultiFieldBatchPutCallback<?> multiFieldBatchPutCallback, int multiFieldBatchPutConsumerThreadCount, int batchPutRetryCount, int batchPutSize, int batchPutTimeLimit, String host, int port, String database, String username, String password, boolean httpCompress, int httpConnectionPool, int httpConnectTimeout, int ioThreadCount, boolean virtualDomainSwitch, RowPointConverter rowConverter, RowTypeInfo rowTypeInfo, boolean ignoreWriteError, boolean printPointDetails) {
        this.batchPutBufferSize = batchPutBufferSize;
        this.batchPutCallback = batchPutCallback;
        this.batchPutConsumerThreadCount = batchPutConsumerThreadCount;
        this.multiFieldBatchPutBufferSize = multiFieldBatchPutBufferSize;
        this.multiFieldBatchPutCallback = multiFieldBatchPutCallback;
        this.multiFieldBatchPutConsumerThreadCount = multiFieldBatchPutConsumerThreadCount;
        this.batchPutRetryCount = batchPutRetryCount;
        this.batchPutSize = batchPutSize;
        this.batchPutTimeLimit = batchPutTimeLimit;
        this.host = host;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
        this.httpCompress = httpCompress;
        this.httpConnectionPool = httpConnectionPool;
        this.httpConnectTimeout = httpConnectTimeout;
        this.ioThreadCount = ioThreadCount;
        this.virtualDomainSwitch = virtualDomainSwitch;
        this.rowConverter = rowConverter;
        this.rowTypeInfo = rowTypeInfo;
        this.ignoreWriteError = ignoreWriteError;
        this.printPointDetails = printPointDetails;
    }

    public void configure(Configuration configuration) {
    }

    protected void superOpen(int taskNumber, int numTasks) throws IOException {
        super.open(taskNumber, numTasks);
    }

    public void open(int taskNumber, int numTasks) throws IOException {
        superOpen(taskNumber, numTasks);
        synchronized (HiTSDBOutputFormat.class) {
            TSDBConfig.Builder hiTSDBConfigBuilder = TSDBConfig.address(this.host, this.port).batchPutBufferSize(this.batchPutBufferSize).batchPutConsumerThreadCount(this.batchPutConsumerThreadCount).multiFieldBatchPutBufferSize(this.multiFieldBatchPutBufferSize).multiFieldBatchPutConsumerThreadCount(this.multiFieldBatchPutConsumerThreadCount).batchPutRetryCount(this.batchPutRetryCount).batchPutSize(this.batchPutSize).batchPutTimeLimit(this.batchPutTimeLimit).httpCompress(this.httpCompress).httpConnectionPool(this.httpConnectionPool).httpConnectTimeout(this.httpConnectTimeout).ioThreadCount(this.ioThreadCount);
            if (StringUtils.isNotBlank(this.username) && StringUtils.isNotBlank(this.password))
                hiTSDBConfigBuilder.basicAuth(this.username, this.password);
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
            TSDBConfig config = hiTSDBConfigBuilder.config();
            this.configStr = toString(config);
            this.exceptionRef = new AtomicReference<>(null);
            if (tsdbConnectionPool.contains(this.configStr)) {
                this.tsdb = tsdbConnectionPool.get(this.configStr);
            } else {
                this.tsdb = TSDBClientFactory.connect(config);
                tsdbConnectionPool.put(this.configStr, this.tsdb);
            }
            if (StringUtils.isNotBlank(this.database))
                this.tsdb.useDatabase(this.database);
        }
        this.outTps = MetricUtils.registerOutTps(getRuntimeContext());
        this.outBps = MetricUtils.registerOutBps(getRuntimeContext(), "hitsdb");
        this.latencyGauge = MetricUtils.registerOutLatency(getRuntimeContext());
    }

    protected void checkExceptionRef() {
        if (getExceptionRef() != null) {
            RuntimeException runtimeException = new RuntimeException(getExceptionRef());
            setExceptionRef(null);
            throw runtimeException;
        }
    }

    protected String toString(TSDBConfig config) {
        return "[putRequestLimit=" + config.getPutRequestLimit() + ", putRequestLimitSwitch=" + config
                .isPutRequestLimitSwitch() + ", batchPutBufferSize=" + config
                .getBatchPutBufferSize() + ", batchPutConsumerThreadCount=" + config
                .getBatchPutConsumerThreadCount() + ", batchPutRetryCount=" + config
                .getBatchPutRetryCount() + ", batchPutSize=" + config
                .getBatchPutSize() + ", batchPutTimeLimit=" + config
                .getBatchPutTimeLimit() + ", host=" + config
                .getHost() + ", httpCompress=" + config
                .isHttpCompress() + ", httpConnectionPool=" + config
                .getHttpConnectionPool() + ", httpConnectTimeout=" + config
                .getHttpConnectTimeout() + ", httpConnectionLiveTime=" + config
                .getHttpKeepaliveTime() + ", httpKeepaliveTime=" + config
                .getHttpKeepaliveTime() + ", ioThreadCount=" + config
                .getIoThreadCount() + ", backpressure=" + config
                .isBackpressure() + ", port=" + config
                .getPort() + ", asyncPut=" + config
                .isAsyncPut() + "]";
    }

    public void close() throws IOException {
        synchronized (HiTSDBOutputFormat.class) {
            if (tsdbConnectionPool.remove(this.configStr) && this.tsdb != null)
                this.tsdb.close();
            checkExceptionRef();
        }
    }

    public void writeAddRecord(Row row) throws IOException {
        if (null != row) {
            AbstractPoint point;
            checkExceptionRef();
            long start = System.currentTimeMillis();
            try {
                point = this.rowConverter.convert(row);
            } catch (Exception e) {
                LOG.error("failed to convert row : " + row.toString());
                throw new RuntimeException("failed to convert row : " + row.toString());
            }
            PointPutUtils.putPoint(this.tsdb, point, false, this.ignoreWriteError);
            long end = System.currentTimeMillis();
            this.outTps.markEvent();
            this.outBps.markEvent(1000L);
            this.latencyGauge.report(end - start);
        }
        checkExceptionRef();
    }

    public void writeDeleteRecord(Row row) throws IOException {
    }

    public String getName() {
        return "hitsdb";
    }

    public Exception getExceptionRef() {
        return this.exceptionRef.get();
    }

    public void setExceptionRef(Exception e) {
        this.exceptionRef.set(e);
    }

    public void sync() throws IOException {
        this.tsdb.flush();
    }

    public static class Builder {
        protected int batchPutBufferSize = 10000;

        protected int multiFieldBatchPutBufferSize = 10000;

        protected AbstractBatchPutCallback<?> batchPutCallback;

        protected AbstractMultiFieldBatchPutCallback<?> multiFieldBatchPutCallback;

        protected int batchPutConsumerThreadCount = 1;

        protected int multiFieldBatchPutConsumerThreadCount = 1;

        protected int batchPutRetryCount = 0;

        protected int batchPutSize = 500;

        protected int batchPutTimeLimit = 200;

        protected String host;

        protected int port = 8242;

        protected String database;

        protected String username;

        protected String password;

        protected boolean httpCompress = false;

        protected int httpConnectionPool = 10;

        protected int httpConnectTimeout = 0;

        protected int ioThreadCount = 1;

        protected boolean virtualDomainSwitch = false;

        protected boolean checkValid = true;

        protected boolean ignoreWriteError = false;

        protected boolean printPointDetails = false;

        protected RowTypeInfo rowTypeInfo;

        protected RowPointConverter rowConverter;

        public Builder setRowConverter(RowPointConverter rowConverter) {
            this.rowConverter = rowConverter;
            return this;
        }

        public Builder setBatchPutBufferSize(int batchPutBufferSize) {
            this.batchPutBufferSize = batchPutBufferSize;
            return this;
        }

        public Builder setMultiFieldBatchPutBufferSize(int multiFieldBatchPutBufferSize) {
            this.multiFieldBatchPutBufferSize = multiFieldBatchPutBufferSize;
            return this;
        }

        public Builder setBatchPutCallback(AbstractBatchPutCallback<?> batchPutCallback) {
            this.batchPutCallback = batchPutCallback;
            return this;
        }

        public Builder setBatchPutConsumerThreadCount(int batchPutConsumerThreadCount) {
            this.batchPutConsumerThreadCount = batchPutConsumerThreadCount;
            return this;
        }

        public Builder setMultiFieldBatchPutConsumerThreadCount(int multiFieldBatchPutConsumerThreadCount) {
            this.multiFieldBatchPutConsumerThreadCount = multiFieldBatchPutConsumerThreadCount;
            return this;
        }

        public Builder setMultiFieldBatchPutCallback(AbstractMultiFieldBatchPutCallback<?> multiFieldBatchPutCallback) {
            this.multiFieldBatchPutCallback = multiFieldBatchPutCallback;
            return this;
        }

        public Builder setBatchPutRetryCount(int batchPutRetryCount) {
            this.batchPutRetryCount = batchPutRetryCount;
            return this;
        }

        public Builder setBatchPutSize(int batchPutSize) {
            this.batchPutSize = batchPutSize;
            return this;
        }

        public Builder setBatchPutTimeLimit(int batchPutTimeLimit) {
            this.batchPutTimeLimit = batchPutTimeLimit;
            return this;
        }

        public Builder setHost(String host) {
            this.host = host;
            return this;
        }

        public Builder setPort(int port) {
            this.port = port;
            return this;
        }

        public Builder setDatabase(String database) {
            this.database = database;
            return this;
        }

        public Builder setUsername(String username) {
            this.username = username;
            return this;
        }

        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        public Builder setHttpCompress(boolean httpCompress) {
            this.httpCompress = httpCompress;
            return this;
        }

        public Builder setHttpConnectionPool(int httpConnectionPool) {
            this.httpConnectionPool = httpConnectionPool;
            return this;
        }

        public Builder setHttpConnectTimeout(int httpConnectTimeout) {
            this.httpConnectTimeout = httpConnectTimeout;
            return this;
        }

        public Builder setIoThreadCount(int ioThreadCount) {
            this.ioThreadCount = ioThreadCount;
            return this;
        }

        public Builder setVirtualDomainSwitch(boolean virtualDomainSwitch) {
            this.virtualDomainSwitch = virtualDomainSwitch;
            return this;
        }

        public Builder setRowTypeInfo(RowTypeInfo rowTypeInfo) {
            this.rowTypeInfo = rowTypeInfo;
            return this;
        }

        public Builder setCheckValid(boolean checkValid) {
            this.checkValid = checkValid;
            return this;
        }

        public Builder setIgnoreWriteError(boolean ignoreWriteError) {
            this.ignoreWriteError = ignoreWriteError;
            return this;
        }

        public Builder setPrintPointDetails(boolean printPointDetails) {
            this.printPointDetails = printPointDetails;
            return this;
        }

        public HiTSDBOutputFormat build() {
            if (this.rowConverter == null)
                this.rowConverter = RowPointConverterFactory.create(this.rowTypeInfo, this.checkValid);
            return new HiTSDBOutputFormat(this.batchPutBufferSize, this.batchPutCallback, this.batchPutConsumerThreadCount, this.multiFieldBatchPutBufferSize, this.multiFieldBatchPutCallback, this.multiFieldBatchPutConsumerThreadCount, this.batchPutRetryCount, this.batchPutSize, this.batchPutTimeLimit, this.host, this.port, this.database, this.username, this.password, this.httpCompress, this.httpConnectionPool, this.httpConnectTimeout, this.ioThreadCount, this.virtualDomainSwitch, this.rowConverter, this.rowTypeInfo, this.ignoreWriteError, this.printPointDetails);
        }
    }
}
