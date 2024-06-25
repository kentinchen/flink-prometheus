SET 'pipeline.name' = 'rds_slowsql_2_perf_prom';

CREATE TEMPORARY TABLE prd_rds_sql (
    `__topic__`                       VARCHAR,
    `__tag__`                          VARCHAR,
    _execute_target_schema          VARCHAR,
    `_execute_source_ip`            VARCHAR,
    _sql_cost_time                  DOUBLE,
    `_sql_rendered`                 VARCHAR,
    `_sql_update_rows`                VARCHAR,
    `_execute_source_user`          VARCHAR,
--    insName                         VARCHAR,
    `_schemaType`                     VARCHAR
) WITH (
    'connector' = 'sls',
    'endPoint' = 'http://data.cn-cq-xczwy-d01.sls.res.alicloud.cqxczwy.com',
    'accessId' = 'x4dwZMvNmOf6o1Ea',
    'accessKey' = '0JK3lGglia3npVfr4FmRKJsJZYFhp1',
    'project' = 'rds-project',
    'logStore' = 'redline-logstore',
    'batchGetSize' = '1000'
);


CREATE TEMPORARY TABLE sw_slowsql (
    instanceId              VARCHAR,
    tag                   VARCHAR,
    db                      VARCHAR,
    ip                      VARCHAR,
    update_rows             VARCHAR,
    sql_rendered            VARCHAR,
    `user`                  VARCHAR,
    schema_type             VARCHAR,
    product                 VARCHAR,
        m                       VARCHAR,
        v                       DOUBLE
--    sql_cost_time           double,
--    create_time             date
) WITH (
      'connector' = 'pushgateway',
      'format' = 'json',
      'pushgateway' = '10.48.4.25:9091',
      'prom.debug' = 'F',
      'prom.method' = 'direct',
      'prom.batch_size' = '100'
  );


--) WITH (
--    'connector' = 'jdbc',
--    'url' = 'jdbc:mysql://pxc-s-unr21qvmsvitqj.mysql.rds.ops.alicloud.cqxczwy.com:3306/monitor',
--    'table-name' = 'slow_sql',
--    'username' = 'grafana',
--    'password' = 'Grafana_'
--  );


insert into sw_slowsql
select
    a.`__topic__`  AS instanceId,
    a.`__tag__`  as tag ,
    a._execute_target_schema  as db,
    if (a._execute_source_ip is not null ,a._execute_source_ip ,'null') as `ip`       ,
    if (a._sql_update_rows is not null ,a._sql_update_rows , '0') as update_rows,
--    a._sql_rendered      ,
    if (CHAR_LENGTH(a._sql_rendered) > 3000 ,CONCAT(SUBSTRING(a._sql_rendered, 1, 3000), '...(SQL too long truncated)') ,a._sql_rendered) as _sql_rendered,
    if (a._execute_source_user is not null ,a._execute_source_user ,'null') as `user`,
    a._schemaType,
    'slow_sql' as product,
    'slow_sql' as m ,
    a._sql_cost_time as v
--    now() as create_time
from prd_rds_sql a
where a.`_sql_rendered` <> 'EXECUTE stmt;' and a._sql_rendered <> 'DEALLOCATE PREPARE stmt;'
and a._sql_rendered <> 'PREPARE stmt FROM @bm_total;' and a._sql_rendered <> 'PREPARE stmt FROM @lic_total;'
and a._sql_rendered not like '%bladex%'
and a._sql_cost_time > 5000000
--and a._sql_cost_time > 5000
;

