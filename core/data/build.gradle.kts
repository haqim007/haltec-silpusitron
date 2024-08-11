plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.devtoolsKSP)
    alias(libs.plugins.jetbrainsKotlinParcelize)
    // alias(libs.plugins.dagger.hilt.android)
    alias(libs.plugins.kotlin.serialization)
}

apply(from = "../../shared-dependencies.gradle")

android {
    namespace = "com.silpusitron.core.data"
    compileSdk = 34

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
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
    // network
    api(libs.ktor.client.core)
    api(libs.ktor.client.okhttp)
    api(libs.ktor.kotlinSerialization)
    api(libs.ktor.contentNegotiation)
    implementation(libs.ktor.cio)
    implementation(libs.ktor.client.logging)
    api(libs.datastore.preferences)
    implementation(libs.kotlinx.serialization.json)
//    debugImplementation(libs.chucker)
//    releaseImplementation(libs.chucker.noop)
    //api(libs.ktor.auth)
    //implementation(libs.retrofit)
    // implementation(libs.converter.gson)
    // implementation(libs.okhttp)
    //implementation(libs.logging.interceptor)

}