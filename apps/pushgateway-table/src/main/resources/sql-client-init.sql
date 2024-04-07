SET sql-client.execution.result-mode = 'tableau';

CREATE CATALOG monitor_catalog WITH(
    'type' = 'jdbc',
    'default-database' = 'monitor',
    'username' = 'monitor',
    'password' = '123456',
    'base-url' = 'jdbc:mysql://10.81.200.185:3306'
);

use catalog monitor_catalog;
