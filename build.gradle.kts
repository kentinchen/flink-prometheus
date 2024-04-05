buildscript {
    val kotlin_version = "1.9.23"
    repositories {
        maven {
            //allowInsecureProtocol = true
            url = properties["MAVEN_REPO"]?.let { uri(it) }!!
        }
        google()
        mavenCentral()
    }
    dependencies {
        // classpath 'com.android.tools.build:gradle:3.6.3'
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

plugins {
    java
    id("com.github.spotbugs") version "6.0.9"
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

