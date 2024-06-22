package com.haltec.silpusitron.feature.auth.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.haltec.silpusitron.feature.auth.common.data.AuthRepository
import com.haltec.silpusitron.feature.auth.common.data.preference.AuthPreference
import com.haltec.silpusitron.feature.auth.common.data.remote.AuthRemoteDataSource
import com.haltec.silpusitron.feature.auth.common.data.remote.AuthService
import com.haltec.silpusitron.feature.auth.common.domain.CheckSessionUseCase
import com.haltec.silpusitron.feature.auth.common.domain.IAuthRepository
import com.haltec.silpusitron.feature.auth.common.domain.LogoutUseCase
import com.haltec.silpusitron.feature.auth.login.domain.usecase.LoginUseCase
import com.haltec.silpusitron.feature.auth.login.ui.LoginViewModel
import com.haltec.silpusitron.feature.auth.otp.domain.usecase.RequestOTPUseCase
import com.haltec.silpusitron.feature.auth.otp.domain.usecase.VerifyOTPUseCase
import com.haltec.silpusitron.feature.auth.otp.ui.OTPViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
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


val authModule = module {
    single(devicePreference) { provideDeviceDataStore(androidContext()) }
    single(authPreference) { provideAuthDataStore(androidContext()) }
    single<AuthPreference>{ AuthPreference(get(named("AuthPreference"))) }
    factory { AuthRemoteDataSource(get()) }
    factory { AuthService(getProperty("BASE_URL"), getProperty("API_VERSION")) }
    factory <IAuthRepository>{ AuthRepository(get(), get(), get()) }
    factory <LoginUseCase> { LoginUseCase() }
    factory <LogoutUseCase> { LogoutUseCase() }
    factory <CheckSessionUseCase> { CheckSessionUseCase() }
    factory { RequestOTPUseCase() }
    factory { VerifyOTPUseCase() }
    viewModel { LoginViewModel(get()) }
    viewModel { OTPViewModel(get(), get(), get(), get()) }
}