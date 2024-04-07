package org.flink.connectors.hitsdb.sink;

import com.alibaba.blink.connectors.hitsdb.Callback;
import com.alibaba.blink.connectors.hitsdb.PointPutUtils;
import com.alibaba.blink.streaming.connectors.common.util.ConnectionPool;
import com.aliyun.hitsdb.client.Config;
import com.aliyun.hitsdb.client.TSDB;
import com.aliyun.hitsdb.client.TSDBClientFactory;
import com.aliyun.hitsdb.client.TSDBConfig;
import com.aliyun.hitsdb.client.callback.AbstractBatchPutCallback;
import com.aliyun.hitsdb.client.callback.AbstractMultiFieldBatchPutCallback;
import com.aliyun.hitsdb.client.value.request.AbstractPoint;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.flink.connectors.hitsdb.ExceptionHolder;

import java.util.concurrent.atomic.AtomicReference;

public abstract class HiTSDBBaseSinkFunction<IN> extends RichSinkFunction<IN> implements ExceptionHolder {
  private static final ConnectionPool<TSDB> tsdbConnectionPool = new ConnectionPool();
  
  private transient TSDB tsdb;
  
  private final String host;
  
  private int batchPutBufferSize = 10000;
  
  private AbstractBatchPutCallback<?> batchPutCallback;
  
  private int batchPutConsumerThreadCount = 1;
  
  private int multiFieldBatchPutBufferSize = 10000;
  
  private transient AbstractMultiFieldBatchPutCallback<?> multiFieldBatchPutCallback;
  
  private int multiFieldBatchPutConsumerThreadCount = 1;
  
  private int batchPutRetryCount = 0;
  
  private int batchPutSize = 500;
  
  private int batchPutTimeLimit = 200;
  
  private int port = 8242;
  
  private String database;
  
  private String username;
  
  private String password;
  
  private boolean httpCompress = false;
  
  private int httpConnectionPool = 10;
  
  private int httpConnectTimeout = 0;
  
  private int ioThreadCount = 1;
  
  private boolean virtualDomainSwitch = false;
  
  private String configStr;
  
  private boolean ignoreWriteError;
  
  private boolean printPointDetails;
  
  private transient AtomicReference<Exception> exceptionRef;
  
  public void setBatchPutBufferSize(int batchPutBufferSize) {
    this.batchPutBufferSize = batchPutBufferSize;
  }
  
  public void setBatchPutCallback(AbstractBatchPutCallback<?> batchPutCallback) {
    this.batchPutCallback = batchPutCallback;
  }
  
  public void setBatchPutConsumerThreadCount(int batchPutConsumerThreadCount) {
    this.batchPutConsumerThreadCount = batchPutConsumerThreadCount;
  }
  
  public void setMultiFieldBatchPutBufferSize(int multiFieldBatchPutBufferSize) {
    this.multiFieldBatchPutBufferSize = multiFieldBatchPutBufferSize;
  }
  
  public void setMultiFieldBatchPutConsumerThreadCount(int multiFieldBatchPutConsumerThreadCount) {
    this.multiFieldBatchPutConsumerThreadCount = multiFieldBatchPutConsumerThreadCount;
  }
  
  public void setMultiFieldBatchPutCallback(AbstractMultiFieldBatchPutCallback<?> multiFieldBatchPutCallback) {
    this.multiFieldBatchPutCallback = multiFieldBatchPutCallback;
  }
  
  public void setBatchPutRetryCount(int batchPutRetryCount) {
    this.batchPutRetryCount = batchPutRetryCount;
  }
  
  public void setBatchPutSize(int batchPutSize) {
    this.batchPutSize = batchPutSize;
  }
  
  public void setBatchPutTimeLimit(int batchPutTimeLimit) {
    this.batchPutTimeLimit = batchPutTimeLimit;
  }
  
  public void setPort(int port) {
    this.port = port;
  }
  
  public void setHttpCompress(boolean httpCompress) {
    this.httpCompress = httpCompress;
  }
  
  public void setHttpConnectionPool(int httpConnectionPool) {
    this.httpConnectionPool = httpConnectionPool;
  }
  
  public void setHttpConnectTimeout(int httpConnectTimeout) {
    this.httpConnectTimeout = httpConnectTimeout;
  }
  
  public void setIoThreadCount(int ioThreadCount) {
    this.ioThreadCount = ioThreadCount;
  }
  
  public void setVirtualDomainSwitch(boolean virtualDomainSwitch) {
    this.virtualDomainSwitch = virtualDomainSwitch;
  }
  
  public void setDatabase(String database) {
    this.database = database;
  }
  
  public void setUsername(String username) {
    this.username = username;
  }
  
  public void setPassword(String password) {
    this.password = password;
  }
  
  public void setIgnoreWriteError(boolean ignoreWriteError) {
    this.ignoreWriteError = ignoreWriteError;
  }
  
  public void setPrintPointDetails(boolean printPointDetails) {
    this.printPointDetails = printPointDetails;
  }
  
  public HiTSDBBaseSinkFunction(String host) {
    this.host = host;
  }
  
  public Exception getExceptionRef() {
    return this.exceptionRef.get();
  }
  
  public void setExceptionRef(Exception e) {
    this.exceptionRef.set(e);
  }
  
  public void open(Configuration parameters) throws Exception {
    super.open(parameters);
    synchronized (HiTSDBBaseSinkFunction.class) {
      TSDBConfig.Builder hiTSDBConfigBuilder = TSDBConfig.address(this.host, this.port).batchPutBufferSize(this.batchPutBufferSize).batchPutConsumerThreadCount(this.batchPutConsumerThreadCount).multiFieldBatchPutBufferSize(this.multiFieldBatchPutBufferSize).multiFieldBatchPutConsumerThreadCount(this.multiFieldBatchPutConsumerThreadCount).batchPutRetryCount(this.batchPutRetryCount).batchPutSize(this.batchPutSize).batchPutTimeLimit(this.batchPutTimeLimit).listenBatchPut(this.batchPutCallback).httpCompress(this.httpCompress).httpConnectionPool(this.httpConnectionPool).httpConnectTimeout(this.httpConnectTimeout).ioThreadCount(this.ioThreadCount);
      if (StringUtils.isNotBlank(this.username) && StringUtils.isNotBlank(this.password))
        hiTSDBConfigBuilder.basicAuth(this.username, this.password); 
      Callback callback = new Callback(this, this.printPointDetails);
      if (!this.ignoreWriteError) {
        this.batchPutCallback = (AbstractBatchPutCallback<?>)callback.batchPutCallback;
        this.multiFieldBatchPutCallback = (AbstractMultiFieldBatchPutCallback<?>)callback.multiFieldBatchPutCallback;
        hiTSDBConfigBuilder.listenBatchPut((AbstractBatchPutCallback)callback.batchPutCallback);
        hiTSDBConfigBuilder.listenMultiFieldBatchPut((AbstractMultiFieldBatchPutCallback)callback.multiFieldBatchPutCallback);
      } else {
        this.batchPutCallback = (AbstractBatchPutCallback<?>)callback.batchPutIgnoreErrorsCallback;
        this.multiFieldBatchPutCallback = (AbstractMultiFieldBatchPutCallback<?>)callback.multiFieldBatchPutIgnoreErrorsCallback;
        hiTSDBConfigBuilder.listenBatchPut((AbstractBatchPutCallback)callback.batchPutIgnoreErrorsCallback);
        hiTSDBConfigBuilder.listenMultiFieldBatchPut((AbstractMultiFieldBatchPutCallback)callback.multiFieldBatchPutIgnoreErrorsCallback);
      } 
      TSDBConfig config = hiTSDBConfigBuilder.config();
      this.configStr = config.toString();
      this.exceptionRef = new AtomicReference<>(null);
      if (tsdbConnectionPool.contains(this.configStr)) {
        this.tsdb = (TSDB)tsdbConnectionPool.get(this.configStr);
      } else {
        this.tsdb = TSDBClientFactory.connect((Config)config);
        tsdbConnectionPool.put(this.configStr, this.tsdb);
      } 
      if (StringUtils.isNotBlank(this.database))
        this.tsdb.useDatabase(this.database); 
    } 
  }
  
  public void invoke(IN value, Context context) throws Exception {
    AbstractPoint point = constructPoint(value);
    PointPutUtils.putPoint(this.tsdb, point, false, false);
  }
  
  public void close() throws Exception {
    synchronized (HiTSDBBaseSinkFunction.class) {
      if (tsdbConnectionPool.remove(this.configStr) && this.tsdb != null)
        this.tsdb.close(); 
    } 
  }
  
  abstract AbstractPoint constructPoint(IN paramIN);
}
