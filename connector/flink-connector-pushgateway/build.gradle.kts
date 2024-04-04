plugins {
    id("java")
}

group = properties["GROUP"] as String + ".connectors.pushgateway"
version = properties["VERSION"]!!

repositories {
    mavenCentral()
}
val flinkVersion = "1.19.0"
val log4jVersion = "2.17.2"
val lombokVersion = "1.18.22"
val junit5Version = "5.10.1"
val assertjCoreVersion = "3.21.0"
val mockitoVersion = "4.0.0"
val mockitoInlineVersion = "4.6.1"
val wiremockVersion = "2.27.2"

dependencies {
    compileOnly("org.apache.flink:flink-java:${flinkVersion}")
    compileOnly("org.apache.flink:flink-clients:${flinkVersion}")
    compileOnly("org.apache.logging.log4j:log4j-slf4j-impl:${log4jVersion}")
    compileOnly("org.apache.logging.log4j:log4j-api:${log4jVersion}")
    compileOnly("org.apache.logging.log4j:log4j-core:${log4jVersion}")
    compileOnly("org.apache.flink:flink-table-api-java-bridge:${flinkVersion}")
    compileOnly("org.apache.flink:flink-connector-base:${flinkVersion}")
    compileOnly("org.projectlombok:lombok:${lombokVersion}")

    testImplementation("org.apache.httpcomponents:httpclient:4.5.13")
    testImplementation("org.apache.flink:flink-test-utils:${flinkVersion}")
    testImplementation("org.apache.flink:flink-table-test-utils:${flinkVersion}")
    testImplementation("org.apache.flink:flink-table-common:${flinkVersion}")
    testImplementation("org.apache.flink:flink-connector-base:${flinkVersion}")
    testImplementation("org.apache.flink:flink-test-utils:${flinkVersion}")
    testImplementation("org.apache.flink:flink-streaming-java:${flinkVersion}")
    testImplementation("org.apache.flink:flink-runtime:${flinkVersion}")
    testImplementation("org.apache.flink:flink-json:${flinkVersion}")
    testImplementation("org.apache.flink:flink-runtime-web:${flinkVersion}")
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junit5Version")
    //testImplementation("org.junit.jupiter:junit-vintage-engine:$junit5Version")
    testImplementation("org.assertj:assertj-core:$assertjCoreVersion")
    testImplementation("org.mockito:mockito-junit-jupiter:$mockitoVersion")
    testImplementation("org.mockito:mockito-inline:$mockitoInlineVersion")
    testImplementation("com.github.tomakehurst:wiremock:$wiremockVersion")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testRuntimeOnly("org.slf4j:slf4j-simple:2.0.12")
}

tasks.test {
    useJUnitPlatform()
}