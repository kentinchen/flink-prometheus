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
    'endPoint' = 'http://data.cn-yaan-sczwhlw-d01.sls.res.inter-sctyun.com',
    'accessid' = 'OLIDBuPuCROXJt52',
    'accesskey' = '9LoXpXIN5by9M8Gzz2CWgSIkGIbxyI',
    'project' = 'aliyun-oss',
    'logstore' = 'oss_monitor_service',
    'batchgetsize' = '1000'
);

CREATE TEMPORARY TABLE oss_perf_prom (
    instanceid             VARCHAR,
    `timestamp`            BIGINT,
    product                VARCHAR,
    m                      VARCHAR,
    v                      DOUBLE
) WITH (
      'connector' = 'pushgateway',
      'format' = 'json',
      'pushgateway' = '10.81.200.185:9091',
      'prom.ts.name' = 'timestamp'
);

INSERT INTO oss_perf_prom
select * from (
    select concat(bucket_name,'') as instanceid,`timestamp`,'oss','recv_size' as m,if(cast(recv_size as varchar)='NaN',cast(0 as DOUBLE),recv_size) as v from oss_perf_sls
    UNION ALL
    select concat(bucket_name,'') as instanceid,`timestamp`,'oss','send_size' as m,if(cast(send_size as varchar)='NaN',cast(0 as DOUBLE),send_size) as v from oss_perf_sls
    UNION ALL
    select concat(bucket_name,'') as instanceid,`timestamp`,'oss','error_403' as m,if(cast(error_403 as varchar)='NaN',cast(0 as DOUBLE),error_403) as v from oss_perf_sls
    UNION ALL
    select concat(bucket_name,'') as instanceid,`timestamp`,'oss','error_404' as m,if(cast(error_404 as varchar)='NaN',cast(0 as DOUBLE),error_404) as v from oss_perf_sls
    UNION ALL
    select concat(bucket_name,'') as instanceid,`timestamp`,'oss','error_408' as m,if(cast(error_408 as varchar)='NaN',cast(0 as DOUBLE),error_408) as v from oss_perf_sls
    UNION ALL
    select concat(bucket_name,'') as instanceid,`timestamp`,'oss','error_4xx' as m,if(cast(error_4xx as varchar)='NaN',cast(0 as DOUBLE),error_4xx) as v from oss_perf_sls
    UNION ALL
    select concat(bucket_name,'') as instanceid,`timestamp`,'oss','error_503' as m,if(cast(error_503 as varchar)='NaN',cast(0 as DOUBLE),error_503) as v from oss_perf_sls
    UNION ALL
    select concat(bucket_name,'') as instanceid,`timestamp`,'oss','error_5xx' as m,if(cast(error_5xx as varchar)='NaN',cast(0 as DOUBLE),error_5xx) as v from oss_perf_sls
    UNION ALL
    select concat(bucket_name,'') as instanceid,`timestamp`,'oss','success_203' as m,if(cast(success_203 as varchar)='NaN',cast(0 as DOUBLE),success_203) as v from oss_perf_sls
    UNION ALL
    select concat(bucket_name,'') as instanceid,`timestamp`,'oss','success_2xx' as m,if(cast(success_2xx as varchar)='NaN',cast(0 as DOUBLE),success_2xx) as v from oss_perf_sls
    UNION ALL
    select concat(bucket_name,'') as instanceid,`timestamp`,'oss','success_3xx' as m,if(cast(success_3xx as varchar)='NaN',cast(0 as DOUBLE),success_3xx) as v from oss_perf_sls
    UNION ALL
    select concat(bucket_name,'') as instanceid,`timestamp`,'oss','intran_recv_size' as m,if(cast(intran_recv_size as varchar)='NaN',cast(0 as DOUBLE),intran_recv_size) as v from oss_perf_sls
    UNION ALL
    select concat(bucket_name,'') as instanceid,`timestamp`,'oss','intran_send_size' as m,if(cast(intran_send_size as varchar)='NaN',cast(0 as DOUBLE),intran_send_size) as v from oss_perf_sls
);