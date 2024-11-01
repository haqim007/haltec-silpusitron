import org.jetbrains.kotlin.konan.properties.Properties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.devtoolsKSP)
    alias(libs.plugins.jetbrainsKotlinParcelize)
    // alias(libs.plugins.dagger.hilt.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.gms.google.service)
}

apply(from = "../shared-dependencies.gradle")
apply(from = "../shared-ui-dependencies.gradle")


android {
    namespace = "com.silpusitron.app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.silpusitron.app"
        minSdk = 26
        targetSdk = 34
        versionCode = 11
        versionName = "1.0.11"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            manifestPlaceholders["FILE_PROVIDER_AUTHORITY"] = "${defaultConfig.applicationId}.fileprovider"

            val secretPropertiesFile = rootProject.file("app/src/release/secret.properties")
            val secretProperties = Properties().apply {
                load(secretPropertiesFile.inputStream())
            }
            buildConfigField("String", "BASE_URL", secretProperties.getProperty("BASE_URL"))
            buildConfigField("String", "API_VERSION", secretProperties.getProperty("API_VERSION"))
            buildConfigField("Boolean", "IS_OFFICER", secretProperties.getProperty("IS_OFFICER"))
            buildConfigField("String", "RECAPTCHA_KEY_ID", secretProperties.getProperty("RECAPTCHA_KEY_ID"))
        }
        debug {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            applicationIdSuffix = ".debug"
            manifestPlaceholders["FILE_PROVIDER_AUTHORITY"] = "${defaultConfig.applicationId}.debug.fileprovider"

            val secretPropertiesFile = rootProject.file("app/src/debug/secret.properties")
            val secretProperties = Properties().apply {
                load(secretPropertiesFile.inputStream())
            }
            buildConfigField("String", "BASE_URL", secretProperties.getProperty("BASE_URL"))
            buildConfigField("String", "API_VERSION", secretProperties.getProperty("API_VERSION"))
            buildConfigField("Boolean", "IS_OFFICER", secretProperties.getProperty("IS_OFFICER"))
            buildConfigField("String", "RECAPTCHA_KEY_ID", secretProperties.getProperty("RECAPTCHA_KEY_ID"))
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:data"))
    implementation(project(":core:ui"))
    implementation(project(":feature:landingpage"))
    implementation(project(":feature:dashboard"))
    implementation(project(":feature:auth"))
    implementation(project(":feature:confirmprofilecitizen"))
    implementation(project(":feature:home"))
    implementation(project(":feature:requirementdocs"))
    implementation(project(":shared:formprofile"))
    implementation(libs.datastore.preferences)
    implementation(libs.androidx.core.splashscreen)
    implementation(platform(libs.firebase.bom))
}