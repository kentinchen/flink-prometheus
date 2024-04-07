CREATE
TEMPORARY TABLE ecs (
    instanceId             VARCHAR,
    ts                     BIGINT,
    `m`                    VARCHAR,
    netname                VARCHAR,
    v                      VARCHAR,
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

select *
from ecs limit 10;

CREATE
TEMPORARY TABLE kvstore_perf_logstore (
    instanceid             VARCHAR,
    ts                     BIGINT,
    `m`                    VARCHAR,
    o_id                   VARCHAR,
    v                      DOUBLE
) WITH (
    'connector' = 'datahub',
    'endpoint' = 'http://datahub.cn-yaan-sczwhlw-d01.sls.res.inter-sctyun.com',
    'accessId'='DbAggn5r8F65cH8Q',
    'accessKey'='a8GERjIW8sb0fEJNURW8w8kQBimS9B',
    'project' = 'cjh',
    'topic' = 'kvstore_perf_logstore'
);

insert into kvstore_perf_logstore
select instanceId,
       ts,
       RedisMonitorConvert(`m`) as m,
       uuid()                   as o_id,
       v
from kvstore
where v >= 0
  and cast(v as VARCHAR) <> 'NaN';


