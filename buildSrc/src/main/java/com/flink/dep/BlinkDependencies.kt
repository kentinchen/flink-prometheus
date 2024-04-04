package com.flink.dep

object BlinkVersions {
    const val blinkVersion = "1.19.0"
}

object BlinkLibs {
    val flink_java = "org.apache.flink:flink-java:${BlinkVersions.blinkVersion}"
    val flink_client = "org.apache.flink:flink-client:${BlinkVersions.blinkVersion}"
    val flink_connector_base = "org.apache.flink:flink-connector-base:${BlinkVersions.blinkVersion}"
    val flink_stream = "org.apache.flink:flink-streaming-java:${BlinkVersions.blinkVersion}"
    val flink_table_api = "org.apache.flink:flink-table-api-java-bridge:${BlinkVersions.blinkVersion}"
    val flinek_test_utils = "org.apache.flink:flink-test-utils:${BlinkVersions.blinkVersion}"
}