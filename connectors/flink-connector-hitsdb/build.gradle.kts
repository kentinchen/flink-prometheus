plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = properties["GROUP"] as String + ".connectors.pushgateway"
version = properties["VERSION"]!!

val flinkVersion = "1.19.0"
val log4jVersion = "2.17.2"
val lombokVersion = "1.18.22"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("org.apache.flink:flink-java:${flinkVersion}")
    compileOnly("org.apache.flink:flink-clients:${flinkVersion}")
    compileOnly("org.apache.logging.log4j:log4j-slf4j-impl:${log4jVersion}")
    compileOnly("org.apache.logging.log4j:log4j-api:${log4jVersion}")
    compileOnly("org.apache.logging.log4j:log4j-core:${log4jVersion}")
    compileOnly("org.apache.flink:flink-table-api-java-bridge:${flinkVersion}")
    compileOnly("org.apache.flink:flink-connector-base:${flinkVersion}")
    compileOnly("org.apache.flink:flink-table-runtime:${flinkVersion}")
    implementation("com.aliyun:hitsdb-client:0.3.7")


    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}