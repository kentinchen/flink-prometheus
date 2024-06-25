SET 'pipeline.name' = 'oss_perf_prom';

CREATE TEMPORARY TABLE oss_perf_sls (
    recv_size DOUBLE,
    send_size DOUBLE,
    error_403 DOUBLE,
    error_404 DOUBLE,
    error_408 DOUBLE,
    error_499 DOUBLE,
    error_4xx DOUBLE,
    error_503 DOUBLE,
    error_5xx DOUBLE,
    success_203 DOUBLE,
    success_2xx DOUBLE,
    success_3xx DOUBLE,
    intran_recv_size DOUBLE,
    intran_send_size DOUBLE,
    `timestamp` BIGINT,
    bucket_name VARCHAR
) WITH (
    'connector'= 'sls',
    'endPoint' = 'http://data.cn-cq-xczwy-d01.sls.res.alicloud.cqxczwy.com',
    'accessid' = 'y1DnV7EhWcgFIKT7',
    'accesskey' = 'Oem5R4aSZHWAbl27b66DlCKlNv4ORv',
    'project' = 'aliyun-oss',
    'logstore' = 'oss_monitor_service',
    'batchgetsize' = '1000'
);

CREATE TEMPORARY TABLE oss_perf_prom (
    recv_size DOUBLE,
    send_size DOUBLE,
    error_403 DOUBLE,
    error_404 DOUBLE,
    error_408 DOUBLE,
    error_499 DOUBLE,
    error_4xx DOUBLE,
    error_503 DOUBLE,
    error_5xx DOUBLE,
    success_203 DOUBLE,
    success_2xx DOUBLE,
    success_3xx DOUBLE,
    intran_recv_size DOUBLE,
    intran_send_size DOUBLE,
    `timestamp` BIGINT,
    product     VARCHAR,
    bucket_name VARCHAR
) WITH (
      'connector' = 'pushgateway',
      'format' = 'json',
      'pushgateway' = '10.48.4.25:9091',
      'prom.ts.name' = 'timestamp',
      'prom.debug' = 'F',
      'prom.mode' = 'column',
      'prom.m.name' = 'recv_size,send_size,error_403,error_404,error_408,error_499,error_4xx,error_503,error_5xx,success_203,success_2xx,success_3xx,intran_recv_size,intran_send_size',
      'prom.batch_size' = '10000'
);

INSERT INTO oss_perf_prom
  select     recv_size ,
             send_size ,
             error_403 ,
             error_404 ,
             error_408 ,
             error_499 ,
             error_4xx ,
             error_503 ,
             error_5xx ,
             success_203 ,
             success_2xx ,
             success_3xx ,
             intran_recv_size ,
             intran_send_size ,
             `timestamp` ,
             'oss' as product,
             bucket_name
   from oss_perf_sls;
