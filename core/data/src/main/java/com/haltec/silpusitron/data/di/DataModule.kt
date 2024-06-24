package com.haltec.silpusitron.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.haltec.silpusitron.data.preference.AuthPreference
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module


// define qualifiers
private val devicePreference = named("DevicePreference")
private val authPreference = named("AuthPreference")

// create DataStore objects using PreferencesDataStore extension property
val Context.deviceDataStore by preferencesDataStore(name = "device_preference")
val Context.authDataStore by preferencesDataStore(name = "auth_preference")

// functions to provide DataStore instances
fun provideDeviceDataStore(context: Context): DataStore<Preferences> {
    return context.deviceDataStore
}

fun provideAuthDataStore(context: Context): DataStore<Preferences> {
    return context.authDataStore
}

val dataModule = module {
    single(devicePreference) { provideDeviceDataStore(androidContext()) }
    single(authPreference) { provideAuthDataStore(androidContext()) }
    single<AuthPreference>{ AuthPreference(get(named("AuthPreference"))) }
}