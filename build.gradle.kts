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
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

//sourceSets {
//    main {
//        java {
//            srcDirs("src/main/java")
//        }
//        resources {
//            srcDirs("src/main/resources")
//            include("**/META-INF/services/*")
//        }
//    }
//}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}


/*tasks.register("clean",Delete::class){
    delete(rootProject.buildDir)
}*/


//tasks.register("copyJars",Copy::class){
//    from 'build/libs'
//    into rootProject . projectDir . absolutePath +'build/libs'
//}

//bootJar.finalizedBy copyJars
//copyJars.mustRunAfter bootJar