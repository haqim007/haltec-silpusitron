package com.silpusitron.shared.auth.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.silpusitron.shared.auth.preference.AuthPreference
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

private val authPreference = named("AuthPreference")

val Context.authDataStore by preferencesDataStore(name = "auth_preference")

fun provideAuthDataStore(context: Context): DataStore<Preferences> {
    return context.authDataStore
}

val authSharedModule = module {
    single(authPreference) { provideAuthDataStore(androidContext()) }
    single<AuthPreference>{
        AuthPreference(
            get(authPreference)
        )
    }
}