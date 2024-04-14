plugins {
    id("java")
}

group = properties["GROUP"] as String + ".app"
version = properties["VERSION"]!!

repositories {
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

val flinkVersion = properties["FLINK_VERSION"]!!

dependencies {
    compileOnly("org.apache.flink:flink-java:$flinkVersion")
    compileOnly("org.apache.flink:flink-streaming-java:$flinkVersion")
    compileOnly("org.apache.flink:flink-table-api-java-bridge:$flinkVersion")
    compileOnly("org.apache.flink:flink-table-runtime:$flinkVersion")
    compileOnly(project(":connectors:flink-connector-pushgateway"))

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.2")
    testImplementation("org.apache.flink:flink-test-utils:$flinkVersion")
    testImplementation("org.assertj:assertj-core:3.25.3")
    testImplementation("org.mockito:mockito-junit-jupiter:5.11.0")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testRuntimeOnly("org.slf4j:slf4j-simple:2.0.12")
}

tasks.test {
    useJUnitPlatform()
}

var copyJar = task("copyJar", type = Copy::class) {
    from("build/libs")
    into(rootProject.projectDir.absolutePath + "/deploy/libs")
    include("*.jar")
}

tasks.named("jar") {
    finalizedBy(copyJar)
}
