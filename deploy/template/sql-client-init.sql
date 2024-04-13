SET sql-client.execution.result-mode = 'tableau';

CREATE CATALOG monitor_catalog WITH(
    'type' = 'jdbc',
    'default-database' = '${mysql_database}',
    'username' = '${mysql_username}',
    'password' = '${mysql_password}',
    'base-url' = 'jdbc:mysql://${mysql_address}'
);

use catalog monitor_catalog;
