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
 'fields.f_sequence.end'='100000',
 'fields.f_random.min'='1',
 'fields.f_random.max'='100',
 'fields.f_random_str.length'='10'
);

CREATE TEMPORARY TABLE gen_perf_print (
  m_name STRING,
  m_value DOUBLE
) WITH (
  'connector'='print'
);

insert into gen_perf_print
select 'gen_metric'                as m_name,
       cast(f_random as DOUBLE)    as m_value
from gen_perf_null;