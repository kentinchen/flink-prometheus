CREATE
TEMPORARY TABLE `mysql_source` (
    `id` INT,
    `name` STRING,
    `score` INT,
    primary key(id) not enforced
) with (
  'connector' = 'jdbc',
  'url' = 'jdbc:mysql://10.81.200.185:3306/monitor',
  'username' = 'monitor',
  'password' = '123456',
  'table-name' = 'score_board_source'
);

CREATE
TEMPORARY TABLE `mysql_target` (
     `id` INT ,
     `name` STRING,
     `score` INT,
     primary key(id) not enforced
) with (
  'connector' = 'jdbc',
  'url' = 'jdbc:mysql://10.81.200.185:3306/monitor',
  'username' = 'monitor',
  'password' = '123456',
  'table-name' = 'score_board_target'
);

insert into mysql_target
select `id`, `name`, `score`
from mysql_source;