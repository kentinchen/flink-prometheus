SET 'pipeline.name' = 'slb_perf_prom';

CREATE TEMPORARY TABLE slb_perf_sls (
    connsPS DOUBLE,
    `count` DOUBLE,
    dropConnPS DOUBLE,
    failConnPS DOUBLE,
    inActConnPS DOUBLE,
    inBitsPS DOUBLE,
    inDBitesPS DOUBLE,
    inDPktsPS DOUBLE,
    inPktsPS DOUBLE,
    maxConnsPs DOUBLE,
    outBitsPS DOUBLE,
    outDBitesPS DOUBLE,
    outDPktsPS DOUBLE,
    outPktsPS DOUBLE,
    sumInBytes DOUBLE,
    sumOutBytes DOUBLE,
    lbId VARCHAR,
    vip VARCHAR,
    msTimestamp BIGINT,
    vport VARCHAR
) WITH (
    'connector' = 'sls',
    'endpoint' = 'http://data.cn-cq-xczwy-d01.sls.res.alicloud.cqxczwy.com',
    'accessid' = 'y1DnV7EhWcgFIKT7',
    'accesskey' = 'Oem5R4aSZHWAbl27b66DlCKlNv4ORv',
    'project' = 'noc',
    'logstore' = 'agg-slb-vs-stats-log',
    'batchGetSize' = '1000'
);

CREATE TEMPORARY TABLE slb_perf_prom (
        connsPS DOUBLE,
        `count` DOUBLE,
        dropConnPS DOUBLE,
        failConnPS DOUBLE,
        inActConnPS DOUBLE,
        inBitsPS DOUBLE,
        inDBitesPS DOUBLE,
        inDPktsPS DOUBLE,
        inPktsPS DOUBLE,
        maxConnsPs DOUBLE,
        outBitsPS DOUBLE,
        outDBitesPS DOUBLE,
        outDPktsPS DOUBLE,
        outPktsPS DOUBLE,
        sumInBytes DOUBLE,
        sumOutBytes DOUBLE,
        lbId VARCHAR,
        vip VARCHAR,
        msTimestamp BIGINT,
        vport VARCHAR,
        product VARCHAR
) WITH (
    'connector' = 'pushgateway',
    'format' = 'json',
    'pushgateway' = '10.48.4.25:9091',
    'prom.debug' = 'F',
    'prom.mode' = 'column',
    'prom.ts.name' = 'msTimestamp',
    'prom.m.name' = 'connsPS,`count`, dropConnPS,failConnPS,\
                     inActConnPS,inBitsPS,inDBitesPS,inDPktsPS,inPktsPS,\
                     maxConnsPs,\
                     outBitsPS,outDBitesPS,outDPktsPS,outPktsPS,\
                     sumInBytes,sumOutBytes',
    'prom.batch_size' = '50000'
);

INSERT INTO slb_perf_prom
select
   connsPS,
   `count` ,
   dropConnPS ,
   failConnPS ,
   inActConnPS ,
   inBitsPS ,
   inDBitesPS ,
   inDPktsPS ,
   inPktsPS ,
   maxConnsPs ,
   outBitsPS ,
   outDBitesPS ,
   outDPktsPS ,
   outPktsPS ,
   sumInBytes ,
   sumOutBytes ,
   lbId ,
   vip ,
   msTimestamp ,
   vport ,
   'slb' as product
from slb_perf_sls;


