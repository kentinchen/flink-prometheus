plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = properties["GROUP"] as String + ".app"
version = properties["VERSION"]!!

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

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

tasks.withType<JavaCompile> {
    options.encoding = "utf-8"
}

var copyJar = task("copyJar", type = Copy::class) {
    from("build/libs")
    into(rootProject.projectDir.absolutePath + "/deploy/libs")
    include("*-all.jar")
    mustRunAfter(tasks.named("shadowJar"))
}

tasks.named("shadowJar") {
    finalizedBy(copyJar)
}