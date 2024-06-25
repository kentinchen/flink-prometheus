SET 'pipeline.name' = 'rds_slowsql_perf_prom';

CREATE TABLE prd_rds_sql (
    __topic__      VARCHAR,
    check_rows     VARCHAR,
    db             VARCHAR,
    fail           VARCHAR,
    `hash`         VARCHAR,
    `ip`           VARCHAR,
    isbind         VARCHAR,
    latency        VARCHAR,
    origin_time    VARCHAR,
    return_rows    VARCHAR,
    `root`         VARCHAR,
    `sql`          VARCHAR,
    tid            VARCHAR,
    update_rows    VARCHAR,
    `user`         VARCHAR
) WITH (
    'connector' = 'sls',
    'endpoint' = 'http://data.cn-cq-xczwy-d01.sls.res.alicloud.cqxczwy.com',
    'accessid' = 'x4dwZMvNmOf6o1Ea',
    'accesskey' = '0JK3lGglia3npVfr4FmRKJsJZYFhp1',
    'project' = 'rds-project',
    'logStore' = 'prd_rds_sql',
    'batchGetSize' = '100'
);


CREATE TEMPORARY TABLE slowsql_perf_logstore (
    __topic__      VARCHAR,
    check_rows     VARCHAR,
    db             VARCHAR,
    fail           VARCHAR,
    `hash`         VARCHAR,
    `ip`           VARCHAR,
    isbind         VARCHAR,
    latency        VARCHAR,
    origin_time    VARCHAR,
    return_rows    VARCHAR,
    `root`         VARCHAR,
    `sql`          VARCHAR,
    tid            VARCHAR,
    update_rows    VARCHAR,
    `user`         VARCHAR,
    m              VARCHAR,
    v              DOUBLE
) WITH (
    'connector' = 'pushgateway',
    'format' = 'json',
    'pushgateway' = '10.48.4.25:9091',
    'prom.debug' = 'F',
    'prom.batch_size' = '100'
);


insert into slowsql_perf_logstore
select
    __topic__    ,
    check_rows   ,
    db           ,
    fail         ,
    `hash`       ,
    `ip`         ,
    isbind       ,
    latency      ,
    origin_time  ,
    return_rows  ,
    `root`       ,
    `sql`        ,
    tid          ,
    update_rows  ,
    `user`       ,
    'slow_sql' as m  ,
    1 as v
from prd_rds_sql;