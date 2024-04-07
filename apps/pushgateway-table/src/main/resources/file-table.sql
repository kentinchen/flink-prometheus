DROP TABLE IF EXISTS employee_information;
CREATE TABLE employee_information
(
    emp_id  INT,
    name    VARCHAR,
    dept_id INT
) WITH (
      'connector' = 'filesystem',
      'path' = '/opt/flink/data/employee.csv',
      'format' = 'csv'
      );
SELECT *
from employee_information
WHERE dept_id = 1;