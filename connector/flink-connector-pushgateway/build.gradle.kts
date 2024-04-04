plugins {
    id("java")
}

group = "com.kentin"
version = "unspecified"

repositories {
    mavenCentral()
}

val flinkVersion = "1.19.0"

dependencies {
    compileOnly("org.apache.flink:flink-java:$flinkVersion")
    compileOnly("org.apache.flink:flink-streaming-java:$flinkVersion")

    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}