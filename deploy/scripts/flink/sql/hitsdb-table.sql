CREATE table stream_hitsdb_amr
(
    metric      VARCHAR,
    `timestamp` INTEGER,
    `value` double,
    cc          VARCHAR,
    mt          VARCHAR,
    mid         VARCHAR,
    mttype      VARCHAR
) WITH (
      'type' = 'custom',
      'tableFactoryClass' = 'com.alibaba.blink.connectors.hitsdb.HiTSDBTableFactory',
      'usePartitionMode' = 'true',
      'partitionKey' = 'mid',
      'partitionHosts' = '25.219.19.129:5566,25.219.19.129:5571,25.219.19.129:5572,25.219.19.129:5573,25.219.19.129:5574,25.219.19.129:5575,25.219.19.129:5576',
      'xxx' = 'xxx')
