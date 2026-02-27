plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.discogsapp.data"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
        buildConfigField(
            "String",
            "DISCOGS_API_KEY",
            "\"${providers.gradleProperty("DISCOGS_API_KEY").orElse("").get()}\"",
        )
        buildConfigField(
            "String",
            "DISCOGS_API_SECRET",
            "\"${providers.gradleProperty("DISCOGS_API_SECRET").orElse("").get()}\"",
        )
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }


    kotlin {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        }
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(project(":domain"))
    implementation(libs.kotlinx.coroutines.core)

    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.moshi)
    implementation(libs.moshi.kotlin)
    implementation(libs.okhttp.logging.interceptor)
    implementation(libs.hilt.android)

    testImplementation(libs.junit4)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockk)
}
