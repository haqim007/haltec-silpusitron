package com.silpusitron.feature.auth.di

import com.silpusitron.feature.auth.common.data.AuthRepository
import com.silpusitron.feature.auth.common.data.remote.AuthRemoteDataSource
import com.silpusitron.feature.auth.common.data.remote.AuthService
import com.silpusitron.feature.auth.common.domain.CheckSessionUseCase
import com.silpusitron.feature.auth.common.domain.IAuthRepository
import com.silpusitron.feature.auth.common.domain.LogoutUseCase
import com.silpusitron.feature.auth.login.domain.usecase.LoginUseCase
import com.silpusitron.feature.auth.login.ui.LoginViewModel
import com.silpusitron.feature.auth.otp.di.otpModule
import com.silpusitron.feature.auth.otp.domain.usecase.RequestOTPUseCase
import com.silpusitron.feature.auth.otp.domain.usecase.VerifyOTPUseCase
import com.silpusitron.feature.auth.otp.ui.OTPViewModel
import com.silpusitron.shared.auth.di.authSharedModule
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val authModule = module {
    includes(authSharedModule, otpModule)
    factory { AuthRemoteDataSource(get()) }
    factory { AuthService(getProperty("BASE_URL"), getProperty("API_VERSION")) }
    factory <IAuthRepository>{ AuthRepository(get(), get(), get()) }
    factory <LoginUseCase> { LoginUseCase() }
    factory <LogoutUseCase> { LogoutUseCase() }
    factory <CheckSessionUseCase> { CheckSessionUseCase() }
    viewModel { LoginViewModel(get()) }
}