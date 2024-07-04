package com.haltec.silpusitron.feature.auth.di

import com.haltec.silpusitron.feature.auth.common.data.AuthRepository
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
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val authModule = module {
    factory { AuthRemoteDataSource(get()) }
    factory { AuthService(getProperty("BASE_URL"), getProperty("API_VERSION")) }
    factory <IAuthRepository>{ AuthRepository(get(), get(), get()) }
    factory <LoginUseCase> { LoginUseCase() }
    factory <LogoutUseCase> { LogoutUseCase() }
    factory <CheckSessionUseCase> { CheckSessionUseCase() }
    factory { RequestOTPUseCase() }
    factory { VerifyOTPUseCase() }
    viewModel { LoginViewModel(get()) }
    viewModel { OTPViewModel(get(), get(), get()) }
}