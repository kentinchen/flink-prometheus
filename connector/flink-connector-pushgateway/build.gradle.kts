plugins {
    id("java")
}

group = "com.kentin"
version = "unspecified"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly(Libs.flink_java)
    compileOnly(Libs.flink_stream)

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.2")
    testImplementation(Libs.flinek_test_utils)
    testImplementation("org.assertj:assertj-core:3.25.3")
    testImplementation("org.mockito:mockito-junit-jupiter:5.11.0")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testRuntimeOnly("org.slf4j:slf4j-simple:2.0.12")
}

tasks.test {
    useJUnitPlatform()
}