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
        env.executeSql(
                "CREATE TEMPORARY TABLE ecs_perf_sls (" +
                        "instanceId             VARCHAR," +
                        "ts                     BIGINT,"  +
                        "m                      VARCHAR," +
                        "netname                VARCHAR," +
                        "v                      VARCHAR," +
                        "mountpoint             VARCHAR," +
                        "diskname               VARCHAR," +
                        "state                  VARCHAR," +
                        "period                 VARCHAR"  +
                        ") WITH (" +
                        "'connector' = 'sls'," +
                        "'endpoint' = 'http://data.cn-yaan-sczwhlw-d01.sls.res.inter-sctyun.com'," +
                        "'accessid' = 'OLIDBuPuCROXJt52'," +
                        "'accesskey' = '9LoXpXIN5by9M8Gzz2CWgSIkGIbxyI'," +
                        "'project' = 'ali-tianji-cms-transfer'," +
                        "'logStore' = 'ecs'" +
                        ");");

        env.executeSql(
                "CREATE TABLE ecs_perf_tsdb ( m_name STRING, report_year STRING, m_value Double)\n"
                        + "WITH (\n"
                        + "  'connector' = 'pushgateway',\n"
                        + "  'format' = 'json',\n"
                        + "  'pushgateway' = '" + pushgateway + "'\n"
                        + ")");

        TableResult tableResult1 = env.executeSql("INSERT INTO ecs_perf_tsdb " +
                "SELECT m as m_name,v as m_value,instanceId " +
                "FROM ecs_perf_sls ");
        System.out.println(tableResult1.getJobClient().get().getJobStatus());

        // since all cluster operations of the Table API are executed asynchronously,
        // we need to wait until the insertion has been completed,
        // an exception is thrown in case of an error
        tableResult1.await();
    }
}
