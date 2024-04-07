plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = properties["GROUP"] as String + ".app"
version = properties["VERSION"]!!

repositories {
    mavenCentral()
}

val flinkVersion = properties["FLINK_VERSION"]!!

dependencies {
    implementation("com.github.jsqlparser:jsqlparser:3.2")
    compileOnly("org.apache.flink:flink-table-runtime:$flinkVersion")

    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

var copyJar = task("copyJar", type = Copy::class) {
    from("build/libs")
    into(rootProject.projectDir.absolutePath + "/libs")
    include("*-all.jar")
    mustRunAfter(tasks.named("shadowJar"))
}

tasks.named("shadowJar") {
    finalizedBy(copyJar)
}