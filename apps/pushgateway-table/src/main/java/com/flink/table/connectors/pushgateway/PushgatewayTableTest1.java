package com.flink.table.connectors.pushgateway;

import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.TableEnvironment;
import org.apache.flink.table.api.TableResult;

public class PushgatewayTableTest1 {

    public static void main(String[] args) throws Exception {
        final ParameterTool params = ParameterTool.fromArgs(args);
        final String pushgateway = params.get("pushgateway", "localhost:9091");
        final EnvironmentSettings settings = EnvironmentSettings.newInstance().inStreamingMode().build();
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
                "CREATE TABLE ecs_perf_tsdb ( m_name STRING, report_year STRING, m_value Double)\n"
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
                                + "  ('LosAngeles',  '2013', 13106100), "
                                + "  ('LosAngeles', '2014', 72600), "
                                + "  ('LosAngeles', '2015', 72300), "
                                + "  ('Chicago', '2013', 9553270), "
                                + "  ('Chicago', '2014', 11340), "
                                + "  ('Chicago', '2015', 6730), "
                                + "  ('Houston', '2013', 6330660), "
                                + "  ('Houston', '2014', 172960), "
                                + "  ('Houston', '2015', 172940), "
                                + "  ('Phoenix', '2013', 4404680), "
                                + "  ('Phoenix', '2014', 86740), "
                                + "  ('Phoenix', '2015', 89700), "
                                + "  ('Dallas',  '2013', 6817520), "
                                + "  ('Dallas',  '2014', 137740), "
                                + "  ('SanAntonio',  '2013', 2280580), "
                                + "  ('SanAntonio',  '2014', 49180), "
                                + "  ('SanAntonio',  '2015', 50870), "
                                + "  ('SanFrancisco',  '2013', 4521310), "
                                + "  ('SanFrancisco',  '2014', 65940), "
                                + "  ('Sanrancisco',  '2015', 62290), "
                                + "  ('Dallas',  '2015', 154020)");

        // since all cluster operations of the Table API are executed asynchronously,
        // we need to wait until the insertion has been completed,
        // an exception is thrown in case of an error
        insertionResult.await();

        //env.execute();
    }
}
