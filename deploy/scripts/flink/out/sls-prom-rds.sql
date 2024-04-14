CREATE TEMPORARY FUNCTION RdsMonitorConvert AS 'org.flink.udf.function.RdsMonitorConvert';
CREATE TEMPORARY FUNCTION dateFormatT08 AS 'org.flink.udf.function.DateFormatT08';

CREATE TEMPORARY TABLE rds_perf_sls (
    instanceId             VARCHAR,
    __timestamp__          BIGINT,
    `metricName`           VARCHAR,
    `uptime`               VARCHAR,
    `type`                 VARCHAR,
    v                      DOUBLE
) WITH (
    'connector'= 'sls',
    'endpoint' = 'http://data.cn-yaan-sczwhlw-d01.sls.res.inter-sctyun.com',
    'accessid' = 'OLIDBuPuCROXJt52',
    'accesskey' = '9LoXpXIN5by9M8Gzz2CWgSIkGIbxyI',
    'project' = 'ali-tianji-cms-transfer',
    'logstore' = 'data-rdsmysql'
);

CREATE TEMPORARY TABLE rds_perf_prom (
    instanceId             VARCHAR,
    ts                     BIGINT,
    m                      VARCHAR,
    product                VARCHAR,
    `type`                 VARCHAR,
    v                      DOUBLE
) WITH (
        'connector' = 'pushgateway',
        'format' = 'json',
        'pushgateway' = '10.81.200.185:9091'
);

insert into rds_perf_prom
select
    instanceId,
    CAST(dateFormatT08(`uptime`) as BIGINT) as ts,
    RdsMonitorConvert(`type`) as m,
    'rds' as product,
    `type`,
    CAST(v as DOUBLE) as v
from rds_perf_sls;