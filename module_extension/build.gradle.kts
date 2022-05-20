plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("maven-publish")
}

/**
 * 统一获取默认依赖
 */
fun safeExtGet(prop: String, fallback: String): String {
    return if (rootProject.ext.has(prop)) {
        rootProject.ext.get(prop) as String
    } else {
        fallback
    }
}

android {
    compileSdk = 31

    defaultConfig {
        minSdk = 21
        targetSdk = 31

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation("androidx.appcompat:appcompat:${safeExtGet("appcompat_version", "1.3.0")}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${safeExtGet("coroutines_version", "1.6.1")}")
}

afterEvaluate {
    publishing {
        publications {
            register<MavenPublication>("release") {
                groupId = "com.yikwing"
                artifactId = "extension"
                version = "1.0"
                afterEvaluate {
                    from(components["release"])
                }
            }
        }
    }
}