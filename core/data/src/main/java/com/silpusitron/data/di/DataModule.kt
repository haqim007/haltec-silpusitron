package com.silpusitron.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module


// define qualifiers
private val devicePreference = named("DevicePreference")

// create DataStore objects using PreferencesDataStore extension property
val Context.deviceDataStore by preferencesDataStore(name = "device_preference")

// functions to provide DataStore instances
fun provideDeviceDataStore(context: Context): DataStore<Preferences> {
    return context.deviceDataStore
}

val dataModule = module {
    single(devicePreference) { provideDeviceDataStore(androidContext()) }
}