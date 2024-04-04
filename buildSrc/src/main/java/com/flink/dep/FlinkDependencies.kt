package com.flink.dep

object FlinkVersions {
    const val flinkVersion = "1.19.0"
}

object FlinkLibs {
    val flink_java = "org.apache.flink:flink-java:${FlinkVersions.flinkVersion}"
    val flink_json = "org.apache.flink:flink-json:${FlinkVersions.flinkVersion}"
    val flink_runtime = "org.apache.flink:flink-runtime:${FlinkVersions.flinkVersion}"
    val flink_runtime_web = "org.apache.flink:flink-runtime-web:${FlinkVersions.flinkVersion}"
    val flink_client = "org.apache.flink:flink-client:${FlinkVersions.flinkVersion}"
    val flink_connector_base = "org.apache.flink:flink-connector-base:${FlinkVersions.flinkVersion}"
    val flink_stream = "org.apache.flink:flink-streaming-java:${FlinkVersions.flinkVersion}"
    val flink_table_api = "org.apache.flink:flink-table-api-java-bridge:${FlinkVersions.flinkVersion}"
    val flinek_test_common = "org.apache.flink:flink-test-common:${FlinkVersions.flinkVersion}"
    val flinek_test_utils = "org.apache.flink:flink-test-utils:${FlinkVersions.flinkVersion}"
    val flinek_test_table_utils = "org.apache.flink:flink-table-test-utils:${FlinkVersions.flinkVersion}"
}