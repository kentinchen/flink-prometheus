CREATE TEMPORARY FUNCTION RedisMonitorConvert AS 'org.flink.udf.function.RedisMonitorConvert';
CREATE TEMPORARY TABLE kv_perf_sls (
    instanceId             VARCHAR,
    ts                     BIGINT,
    `m`                    VARCHAR,
    v                      DOUBLE
) WITH (
    'connector'= 'sls',
    'endPoint' = 'http://data.cn-yaan-sczwhlw-d01.sls.res.inter-sctyun.com',
    'accessid' = 'OLIDBuPuCROXJt52',
    'accesskey' = '9LoXpXIN5by9M8Gzz2CWgSIkGIbxyI',
    'project' = 'ali-tianji-cms-transfer',
    'logstore' = 'kvstore'
);

CREATE TEMPORARY TABLE kv_perf_prom (
    instanceid             VARCHAR,
    ts                     BIGINT,
    `m`                    VARCHAR,
    product                VARCHAR,
    v                      DOUBLE
) WITH (
    'connector' = 'pushgateway',
    'format' = 'json',
    'pushgateway' = '10.81.200.185:9091'
);

insert into kv_perf_prom
select
    instanceId,
    ts,
    RedisMonitorConvert(`m`) as m,
    'kv',
    v
from kv_perf_sls
where v >= 0 and cast(v as VARCHAR) <> 'NaN';