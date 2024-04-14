CREATE TEMPORARY TABLE ecs_perf_sls (
    instanceId             VARCHAR,
    ts                     BIGINT,
    m                      VARCHAR,
    netname                VARCHAR,
    v                      DOUBLE,
    mountpoint             VARCHAR,
    diskname               VARCHAR,
    `state`                VARCHAR,
    `period`               VARCHAR
) WITH (
    'connector' = 'sls',
    'endpoint' = 'http://data.cn-yaan-sczwhlw-d01.sls.res.inter-sctyun.com',
    'accessid' = 'OLIDBuPuCROXJt52',
    'accesskey' = '9LoXpXIN5by9M8Gzz2CWgSIkGIbxyI',
    'project' = 'ali-tianji-cms-transfer',
    'logStore' = 'ecs'
);

CREATE TEMPORARY TABLE ecs_perf_prom (
    instanceId             VARCHAR,
    ts                     BIGINT,
    product                VARCHAR,
    m                      VARCHAR,
    netname                VARCHAR,
    v                      DOUBLE,
    mountpoint             VARCHAR,
    diskname               VARCHAR,
    `state`                VARCHAR,
    `period`               VARCHAR
) WITH (
    'connector' = 'pushgateway',
    'format' = 'json',
    'pushgateway' = '10.81.200.185:9091'
);

insert into ecs_perf_prom
select
    instanceId,
    ts,
    'ecs',
    `m`,
    netname,
    cast(v as DOUBLE),
    mountpoint,
    diskname,
    `state`,
    `period`
from ecs_perf_sls
where cast(v as VARCHAR) <> 'NaN' and cast(v as DOUBLE) >= 0.0;


