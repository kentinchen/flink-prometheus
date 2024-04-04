object Versions {
    const val flinkVersion = "1.19.0"
}

object Libs {
    val flink_java = "org.apache.flink:flink-java:${Versions.flinkVersion}"
    val flink_stream = "org.apache.flink:flink-streaming-java:${Versions.flinkVersion}"
    val flinek_test_utils = "org.apache.flink:flink-test-utils:${Versions.flinkVersion}"
}