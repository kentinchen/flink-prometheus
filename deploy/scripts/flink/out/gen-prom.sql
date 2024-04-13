create TEMPORARY TABLE gen_perf_null (
 f_sequence INT,
 f_random INT,
 f_random_str STRING,
 ts AS localtimestamp,
 WATERMARK FOR ts AS ts
) with (
 'connector' = 'datagen',
 -- optional options --
 'rows-per-second'='5',
 'fields.f_sequence.kind'='sequence',
 'fields.f_sequence.start'='1',
 'fields.f_sequence.end'='10000000',
 'fields.f_random.min'='1',
 'fields.f_random.max'='100',
 'fields.f_random_str.length'='10'
);

create TEMPORARY TABLE gen_perf_prom (
    m                VARCHAR,
    v                DOUBLE
) with (
    'connector' = 'pushgateway',
    'format' = 'json',
    'pushgateway' = '10.81.200.185:9091'
);

insert into gen_perf_prom
select 'gen_metric'                as m,
       cast(f_random as DOUBLE)    as v
from gen_perf_null;