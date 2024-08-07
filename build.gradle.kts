// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.devtoolsKSP) apply false
    alias(libs.plugins.jetbrainsKotlinParcelize) apply false
    // alias(libs.plugins.dagger.hilt.android) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.gms.google.service) apply false
    alias(libs.plugins.google.maps.secret) apply false
}