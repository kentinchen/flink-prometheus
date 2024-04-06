del libs/*.jar
.\gradlew clean
.\gradlew jar
.\gradlew shadow
docker/build-dev.bat
docker-compose up -d

.\gradlew tasks
.\gradlew help --task test
.\gradlew :connectors:flink-connector-socket:jar --info
.\gradlew :apps:prometheus:spotlessApply
.\gradlew spotbugsMain

nc -lk 9999
INSERT|Alice|12

timestamp|m_type|m_name|m_value|vm_id|
1712307830|gauge|ecs_cpu_load1|0.45|i-kentin20240405
1712307830|gauge|ecs_cpu_load1|0.47|i-kentin20240404
timestamp 10位长度时间timestamp,精确到秒,为整数(统一以0时区）
m_type    指标类型，按prometheus四种类型（Counter、Gauge、Histogram、Summary）忽略大小写，一般用gauge
          Histogram：直方图常用于请求持续时间或者响应大小采样，然后将结果记录到存储桶（bucket）,每个桶为累加数据。
                     桶累积计数器：<basename>_bucket，
                     观察值的总和：<basename>_sum，
                     观察事件计数：<basename>_count
                     histogram_quantile计算百分位，histogram_quantile函数聚合
          Summary：与Histogram相似，Summary是直接在客户端计算出了百分位数
                   φ分位数：<basename>{quantile="<φ>"}，
                   观察值总和：<basename>_sum，
                   观察事件数，<basename>_count
m_name    指标名称(例如ecs_cpu_load1),定好之后不能变动，告警与这些指标名强依赖
m_value   指标值（double类型）
其它为动态字段，会打到指标标签中

CREATE TABLE ecs_perf_tsdb(
    ts     INT,
    m_type STRING,
    m_name STRING,
    m_value Double,
    vm_id STRING)
WITH (
  'connector' = 'pushgateway',
  'pushgateway' = 'http://localhost:9091'
)

INSERT INTO ecs_perf_tsdb SELECT ts,m_type,m_name,m_value,vm_id FROM ecs_perf_sls;


Caused by: org.apache.flink.client.program.ProgramInvocationException: The main method caused an error:
Table sink 'default_catalog.default_database.ecs_perf_tsdb' doesn't support consuming delete changes which is produced by node TableSourceScan(table=[[default_catalog, default_database, ecs_perf_sls]], fields=[ts, m_type, m_name, m_value, vm_id])