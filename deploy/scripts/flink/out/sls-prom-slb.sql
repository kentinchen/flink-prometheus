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
    `sumInBytes` DOUBLE,
    `sumOutBytes` DOUBLE,
    lbId VARCHAR,
    vip VARCHAR,
    msTimestamp BIGINT,
    vport VARCHAR
) WITH (
    'connector' = 'sls',
    'endpoint' = 'http://data.cn-yaan-sczwhlw-d01.sls.res.inter-sctyun.com',
    'accessid' = 'OLIDBuPuCROXJt52',
    'accesskey' = '9LoXpXIN5by9M8Gzz2CWgSIkGIbxyI',
    'project' = 'noc',
    'logstore' = 'agg-slb-vs-stats-log',
    'batchGetSize' = '1000'
);

CREATE TEMPORARY TABLE slb_perf_prom (
    instanceid             VARCHAR,
    ts                     BIGINT,
    `product`              VARCHAR,
    `m`                    VARCHAR,
    `port`                 VARCHAR,
    v                      DOUBLE,
    vip                    VARCHAR
) WITH (
    'connector' = 'pushgateway',
    'format' = 'json',
    'pushgateway' = '10.81.200.185:9091'
);

INSERT INTO slb_perf_prom
select * from (
    select lbId as instanceid,`msTimestamp` as ts,'slb','connsPS' as m,vport,if(cast(connsPS as varchar)='NaN',cast(0 as DOUBLE),connsPS) as v,vip from slb_perf_sls
    UNION ALL
    select lbId as instanceid,`msTimestamp` as ts,'slb','count' as m,vport,if(cast(`count` as varchar)='NaN',cast(0 as DOUBLE),`count`) as v,vip from slb_perf_sls
    UNION ALL
    select lbId as instanceid,`msTimestamp` as ts,'slb','dropConnPS' as m,vport,if(cast(`dropConnPS` as varchar)='NaN',cast(0 as DOUBLE),`dropConnPS`) as v,vip from slb_perf_sls
    UNION ALL
    select lbId as instanceid,`msTimestamp` as ts,'slb','failConnPS' as m,vport,if(cast(`failConnPS` as varchar)='NaN',cast(0 as DOUBLE),`failConnPS`) as v,vip from slb_perf_sls
    UNION ALL
    select lbId as instanceid,`msTimestamp` as ts,'slb','inActConnPS' as m,vport,if(cast(`inActConnPS` as varchar)='NaN',cast(0 as DOUBLE),`inActConnPS`) as v,vip from slb_perf_sls
    UNION ALL
    select lbId as instanceid,`msTimestamp` as ts,'slb','inBitsPS' as m,vport,if(cast(`inBitsPS` as varchar)='NaN',cast(0 as DOUBLE),`inBitsPS`) as v,vip from slb_perf_sls
    UNION ALL
    select lbId as instanceid,`msTimestamp` as ts,'slb','inDBitesPS' as m,vport,if(cast(`inDBitesPS` as varchar)='NaN',cast(0 as DOUBLE),`inDBitesPS`) as v,vip from slb_perf_sls
    UNION ALL
    select lbId as instanceid,`msTimestamp` as ts,'slb','inDPktsPS' as m,vport,if(cast(`inDPktsPS` as varchar)='NaN',cast(0 as DOUBLE),`inDPktsPS`) as v,vip from slb_perf_sls
    UNION ALL
    select lbId as instanceid,`msTimestamp` as ts,'slb','inPktsPS' as m,vport,if(cast(`inPktsPS` as varchar)='NaN',cast(0 as DOUBLE),`inPktsPS`) as v,vip from slb_perf_sls
    UNION ALL
    select lbId as instanceid,`msTimestamp` as ts,'slb','maxConnsPs' as m,vport,if(cast(`maxConnsPs` as varchar)='NaN',cast(0 as DOUBLE),`maxConnsPs`) as v,vip from slb_perf_sls
    UNION ALL
    select lbId as instanceid,`msTimestamp` as ts,'slb','outBitsPS' as m,vport,if(cast(`outBitsPS` as varchar)='NaN',cast(0 as DOUBLE),`outBitsPS`) as v,vip from slb_perf_sls
    UNION ALL
    select lbId as instanceid,`msTimestamp` as ts,'slb','outDBitesPS' as m,vport,if(cast(`outDBitesPS` as varchar)='NaN',cast(0 as DOUBLE),`outDBitesPS`) as v,vip from slb_perf_sls
    UNION ALL
    select lbId as instanceid,`msTimestamp` as ts,'slb','outDPktsPS' as m,vport,if(cast(`outDPktsPS` as varchar)='NaN',cast(0 as DOUBLE),`outDPktsPS`) as v,vip from slb_perf_sls
    UNION ALL
    select lbId as instanceid,`msTimestamp` as ts,'slb','outPktsPS' as m,vport,if(cast(`outPktsPS` as varchar)='NaN',cast(0 as DOUBLE),`outPktsPS`) as v,vip from slb_perf_sls
    UNION ALL
    select lbId as instanceid,`msTimestamp` as ts,'slb','sumInBytes' as m,vport,if(cast(`sumInBytes` as varchar)='NaN',cast(0 as DOUBLE),`sumInBytes`) as v,vip from slb_perf_sls
    UNION ALL
    select lbId as instanceid,`msTimestamp` as ts,'slb','sumOutBytes' as m,vport,if(cast(`sumOutBytes` as varchar)='NaN',cast(0 as DOUBLE),`sumOutBytes`) as v,vip from slb_perf_sls
);


