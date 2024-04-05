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