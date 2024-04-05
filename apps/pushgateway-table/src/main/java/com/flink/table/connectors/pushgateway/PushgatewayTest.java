package com.flink.table.connectors.pushgateway;

import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.TableResult;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

public class PushgatewayTest {

    public static void main(String[] args) throws Exception {
        final ParameterTool params = ParameterTool.fromArgs(args);
        final String hostname = params.get("hostname", "localhost");
        final String port = params.get("port", "9999");
        final String pushgateway = params.get("pushgateway", "localhost:9091");

        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1); // source only supports parallelism of 1

        final StreamTableEnvironment tEnv = StreamTableEnvironment.create(env);

        // register a table in the catalog
        tEnv.executeSql(
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
                        + ")");

        tEnv.executeSql(
                "CREATE TABLE ecs_perf_tsdb (" +
                        "ts     INT, " +
                        "m_type STRING, " +
                        "m_name STRING, " +
                        "m_value Double, " +
                        "vm_id STRING)\n"
                        + "WITH (\n"
                        + "  'connector' = 'pushgateway',\n"
                        + "  'pushgateway' = '" + pushgateway + "'\n"
                        + ")");

        TableResult tableResult1 = tEnv.executeSql("INSERT INTO ecs_perf_tsdb SELECT ts,m_type,m_name,m_value,vm_id FROM ecs_perf_sls ");
        System.out.println(tableResult1.getJobClient().get().getJobStatus());

        env.execute();
    }
}
