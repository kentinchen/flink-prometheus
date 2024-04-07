CREATE
TEMPORARY TABLE ecs_perf_sls (
    instanceId             VARCHAR,
    ts                     BIGINT,
    m                      VARCHAR,
    netname                VARCHAR,
    v                      DOUBLE,
    mountpoint             VARCHAR,
    diskname               VARCHAR,
    state                  VARCHAR
) WITH (
    'connector' = 'sls',
    'endpoint' = 'http://data.cn-yaan-sczwhlw-d01.sls.res.inter-sctyun.com',
    'accessid' = 'OLIDBuPuCROXJt52',
    'accesskey' = '9LoXpXIN5by9M8Gzz2CWgSIkGIbxyI',
    'project' = 'ali-tianji-cms-transfer',
    'logStore' = 'ecs'
);

select *
from ecs_perf_sls limit 10;

CREATE
TEMPORARY TABLE ecs_perf_prom (
    m_name                 VARCHAR,
    m_value                DOUBLE,
    oid                    VARCHAR,
    instanceId             VARCHAR
) WITH (
    'connector' = 'pushgateway',
    'format' = 'json',
    'pushgateway' = '10.81.200.185:9091'
);

insert into ecs_perf_prom
select m      as m_name,
       v      as m_value,
       uuid() as oid,
       instanceId
from ecs_perf_sls
where v >= 0
  and cast(v as VARCHAR) <> 'NaN';


