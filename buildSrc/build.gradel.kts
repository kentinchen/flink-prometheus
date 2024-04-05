plugins {
    `kotlin-dsl`
}
repositories {
    jcenter()
    gradlePluginPortal()
}

dependencies {
    implementation("libs.spotbugs.gradle.plugin")
}