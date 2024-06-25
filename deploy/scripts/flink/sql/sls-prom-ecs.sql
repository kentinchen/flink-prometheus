SET 'pipeline.name' = 'ecs_perf_prom';

CREATE TEMPORARY TABLE ecs_perf_sls (
    userId                 VARCHAR,
    instanceId             VARCHAR,
    ts                     BIGINT,
    m                      VARCHAR,
    netname                VARCHAR,
    v                      DOUBLE,
    mountpoint             VARCHAR,
    diskname               VARCHAR,
    `state`                VARCHAR,
    `period`               VARCHAR,
     proctime as PROCTIME()   -- 通过计算列产生一个处理时间列

) WITH (
    'connector' = 'sls',
    'endpoint' = 'http://data.cn-cq-xczwy-d01.sls.res.alicloud.cqxczwy.com',
    'accessid' = 'y1DnV7EhWcgFIKT7',
    'accesskey' = 'Oem5R4aSZHWAbl27b66DlCKlNv4ORv',
    'project' = 'ali-tianji-cms-transfer',
    'logStore' = 'ecs',
    'batchGetSize' = '1000'
);


CREATE TEMPORARY TABLE dim_ecs (
    vm_id                 VARCHAR,  -- 实例id
    vm_ip                 VARCHAR,  -- 实例IP
    vm_name               VARCHAR   -- 实例名称
) WITH (
    'connector' = 'jdbc',
    'url' = 'jdbc:mysql://pxc-s-unr21qvmsvitqj.mysql.rds.ops.alicloud.cqxczwy.com:3306/monitor',
    'table-name' = 'ecs_info',
    'username' = 'grafana',
    'password' = 'Grafana_',
    'lookup.cache.max-rows' = '5000',
    'lookup.cache.ttl' = '10min'
);



CREATE TEMPORARY TABLE ecs_perf_prom (
    userId                 VARCHAR,
    instanceId             VARCHAR,
    instanceIp             VARCHAR,
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
    'pushgateway' = '10.48.4.25:9091',
    'prom.batch_size' = '50000'
);

insert into ecs_perf_prom
select
    userId,
    instanceId,
    if(d.vm_ip is NOT NULL, d.vm_ip, 'no-ip') as instanceIp,
    ts,
    'ecs',
    m,
    netname,
    v,
    mountpoint,
    diskname,
    `state`,
    `period`
from ecs_perf_sls as s
LEFT JOIN dim_ecs FOR SYSTEM_TIME AS OF s.proctime as d
  ON s.instanceId = d.vm_id
where userId <> '1000000007';


