buildscript {
    val kotlin_version = "1.9.23"
    repositories {
        /*maven {
            allowInsecureProtocol = true
            url $MAVEN_REPO
        }*/
        google()
        mavenCentral()
    }
    dependencies {
        // classpath 'com.android.tools.build:gradle:3.6.3'
        // classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version"
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

plugins {
    id("idea")
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

task clean (type: Delete) {
    delete rootProject . buildDir
}

task copyJars (type: Copy){
    from 'build/libs'
    into rootProject . projectDir . absolutePath +'build/libs'
}

//bootJar.finalizedBy copyJars
//copyJars.mustRunAfter bootJar