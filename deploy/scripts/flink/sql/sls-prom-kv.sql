SET 'pipeline.name' = 'kvs_perf_prom';

CREATE TEMPORARY FUNCTION RedisMonitorConvert AS 'org.flink.udf.function.RedisMonitorConvert';
CREATE TEMPORARY TABLE kv_perf_sls (
    instanceId             VARCHAR,
    ts                     BIGINT,
    m                      VARCHAR,
    v                      DOUBLE
) WITH (
    'connector'= 'sls',
    'endPoint' = 'http://data.cn-cq-xczwy-d01.sls.res.alicloud.cqxczwy.com',
    'accessid' = 'y1DnV7EhWcgFIKT7',
    'accesskey' = 'Oem5R4aSZHWAbl27b66DlCKlNv4ORv',
    'project' = 'ali-tianji-cms-transfer',
    'logstore' = 'kvstore'
);

CREATE TEMPORARY TABLE kv_perf_prom (
    instanceId             VARCHAR,
    ts                     BIGINT,
    m                      VARCHAR,
    product                VARCHAR,
    v                      DOUBLE
) WITH (
    'connector' = 'pushgateway',
    'format' = 'json',
    'pushgateway' = '10.48.4.25:9091',
    'prom.batch_size' = '10000'
);

insert into kv_perf_prom
select
    instanceId,
    ts,
    RedisMonitorConvert(m) as m,
    'kv' as product,
    v
from kv_perf_sls;
