plugins {
    id("java")
}

group = properties["GROUP"] as String + ".app"
version = properties["VERSION"]!!

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

var copyJar = task("copyJar", type = Copy::class) {
    from("build/libs")
    into(rootProject.projectDir.absolutePath + "/libs")
    include("*.jar")
}

tasks.named("jar") {
    finalizedBy(copyJar)
}
