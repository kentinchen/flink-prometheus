CREATE TEMPORARY TABLE ecs_perf_sls (
    instanceId             VARCHAR,
    ts                     BIGINT,
    m                      VARCHAR,
    netname                VARCHAR,
    v                      VARCHAR,
    mountpoint             VARCHAR,
    diskname               VARCHAR,
    state                  VARCHAR,
    period                 VARCHAR
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
TEMPORARY TABLE ecs_perf_prom (
    instanceid             VARCHAR,
    `m`                    VARCHAR,
    oid                    VARCHAR,
    v                      DOUBLE
) WITH (
    'connector' = 'pushgateway',
    'format' = 'json',
    'pushgateway' = '10.81.200.185:9091'
);

insert into ecs_perf_prom
select instanceId,
       m                        as m_name,
       uuid()                   as oid,
       v                        as m_value
from ecs_perf_sls
where v >= 0
  and cast(v as VARCHAR) <> 'NaN';


