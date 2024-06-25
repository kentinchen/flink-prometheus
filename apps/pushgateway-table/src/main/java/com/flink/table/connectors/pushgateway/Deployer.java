package com.flink.table.connectors.pushgateway;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

import java.io.File;
import java.util.List;

public class Deployer {

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.out.println("请输入作业文件全路径");
            return;
        }
        String path = args[0];
        File file = new File(path);
        if (!file.exists()) {
            System.out.println("作业文件" + path + "不存在");
            return;
        }

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        StreamTableEnvironment tabEnv = StreamTableEnvironment.create(env);
        List<String> list = FileUtils.readLines(new File(path), "UTF-8");
        StringBuilder stringBuilder = new StringBuilder();
        String sql = "";
        for (String var : list) {
            if (StringUtils.isNotBlank(var)) {
                stringBuilder.append(var);
                if (var.contains(";")) {
                    sql = stringBuilder.toString().replace(";", "");
                    System.out.println(sql);
                    tabEnv.executeSql(sql);
                    stringBuilder = new StringBuilder();
                } else {
                    stringBuilder.append("\n");
                }
            }
        }
    }
}