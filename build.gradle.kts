// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {

    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://jitpack.io")
        }
        mavenLocal()
    }

    dependencies {
        classpath("com.android.tools.build:gradle:7.2.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.0")
        classpath("com.yikwing:jiagu:1.0")


        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

plugins {
    id("com.google.devtools.ksp") version "1.7.0-1.0.6" apply false
    id("org.jetbrains.kotlin.jvm") version "1.7.0" apply false
}

apply(from = "ykext.gradle")

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}