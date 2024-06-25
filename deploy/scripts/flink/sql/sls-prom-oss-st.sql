SET 'pipeline.name' = 'oss_st_perf_prom';

CREATE TABLE oss_monitor_service (
                                     storage DOUBLE,
                                     bucket_name VARCHAR
) WITH (
      'connector'= 'sls',
      'endPoint' = 'http://data.cn-cq-xczwy-d01.sls.res.alicloud.cqxczwy.com',
      'accessid' = 'y1DnV7EhWcgFIKT7',
      'accesskey' = 'Oem5R4aSZHWAbl27b66DlCKlNv4ORv',
      'project' = 'aliyun-oss',
      'logStore' = 'oss_metering_data',
      'batchGetSize' = '100'
      );

CREATE TABLE oss_perf_logstore (
                                   bucket_name             VARCHAR,
                                   m                      VARCHAR,
                                   product                VARCHAR,
                                   v                      DOUBLE
) WITH (
      'connector' = 'pushgateway',
      'format' = 'json',
      'pushgateway' = '10.48.4.25:9091',
      'prom.batch_size' = '100'
      );

INSERT INTO oss_perf_logstore
                  select bucket_name,
                         'storage' as m,
                         'oss' as product,
                         if(cast(storage as varchar)='NaN',cast(0 as DOUBLE),storage) as v
                  from oss_monitor_service



