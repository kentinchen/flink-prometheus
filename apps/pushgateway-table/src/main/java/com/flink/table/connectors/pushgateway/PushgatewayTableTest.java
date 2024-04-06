package com.flink.table.connectors.pushgateway;

import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.TableEnvironment;
import org.apache.flink.table.api.TableResult;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

public class PushgatewayTableTest {

    public static void main(String[] args) throws Exception {
        final ParameterTool params = ParameterTool.fromArgs(args);
        final String hostname = params.get("hostname", "localhost");
        final String port = params.get("port", "9999");
        final String pushgateway = params.get("pushgateway", "localhost:9091");

        /*final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1); // source only supports parallelism of 1
        final StreamTableEnvironment tEnv = StreamTableEnvironment.create(env);*/

        final EnvironmentSettings settings =                EnvironmentSettings.newInstance().inStreamingMode().build();
        final TableEnvironment env = TableEnvironment.create(settings);

        // register a table in the catalog
        /*env.executeSql(
                "CREATE TABLE ecs_perf_sls (" +
                        "ts     INT, " +
                        "m_type STRING, " +
                        "m_name STRING, " +
                        "m_value Double, " +
                        "vm_id STRING)\n"
                        + "WITH (\n"
                        + "  'connector' = 'socket',\n"
                        + "  'hostname' = '"
                        + hostname
                        + "',\n"
                        + "  'port' = '"
                        + port
                        + "',\n"
                        + "  'byte-delimiter' = '10',\n"
                        + "  'format' = 'changelog-csv',\n"
                        + "  'changelog-csv.column-delimiter' = '|'\n"
                        + ")");*/

        env.executeSql(
                "CREATE TABLE ecs_perf_tsdb (" +
                        "m_name STRING, " +
                        "m_value Double, " +
                        "vm_id INT)\n"
                        + "WITH (\n"
                        + "  'connector' = 'pushgateway',\n"
                        + "  'format' = 'json',\n"
                        + "  'pushgateway' = '" + pushgateway + "'\n"
                        + ")");

        /*TableResult tableResult1 = env.executeSql("INSERT INTO ecs_perf_tsdb SELECT ts,m_type,m_name,m_value,vm_id FROM ecs_perf_sls ");
        System.out.println(tableResult1.getJobClient().get().getJobStatus());*/

        // insert some example data into the table
        final TableResult insertionResult =
                env.executeSql(
                        "INSERT INTO ecs_perf_tsdb VALUES"
                                + "  ('Los Angeles',  2013, 13106100), "
                                + "  ('Los Angeles', 2014, 72600), "
                                + "  ('Los Angeles', 2015, 72300), "
                                + "  ('Chicago', 2013, 9553270), "
                                + "  ('Chicago', 2014, 11340), "
                                + "  ('Chicago', 2015, -6730), "
                                + "  ('Houston', 2013, 6330660), "
                                + "  ('Houston', 2014, 172960), "
                                + "  ('Houston', 2015, 172940), "
                                + "  ('Phoenix', 2013, 4404680), "
                                + "  ('Phoenix', 2014, 86740), "
                                + "  ('Phoenix', 2015, 89700), "
                                + "  ('San Antonio',  2013, 2280580), "
                                + "  ('San Antonio',  2014, 49180), "
                                + "  ('San Antonio',  2015, 50870), "
                                + "  ('San Francisco',  2013, 4521310), "
                                + "  ('San Francisco',  2014, 65940), "
                                + "  ('San Francisco',  2015, 62290), "
                                + "  ('Dallas',  2013, 6817520), "
                                + "  ('Dallas',  2014, 137740), "
                                + "  ('Dallas',  2015, 154020)");

        // since all cluster operations of the Table API are executed asynchronously,
        // we need to wait until the insertion has been completed,
        // an exception is thrown in case of an error
        insertionResult.await();

        //env.execute();
    }
}