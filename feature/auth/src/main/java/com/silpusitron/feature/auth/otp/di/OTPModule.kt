package com.silpusitron.feature.auth.otp.di

import com.silpusitron.feature.auth.otp.data.OTPRepository
import com.silpusitron.feature.auth.otp.data.remote.OTPRemoteDataSource
import com.silpusitron.feature.auth.otp.data.remote.OTPService
import com.silpusitron.feature.auth.otp.domain.IOTPRepository
import com.silpusitron.feature.auth.otp.domain.usecase.RequestOTPUseCase
import com.silpusitron.feature.auth.otp.domain.usecase.VerifyOTPUseCase
import com.silpusitron.feature.auth.otp.ui.OTPViewModel
import com.silpusitron.feature.auth.phonenumberupdate.di.phoneNumberUpdateModule
import com.silpusitron.shared.auth.di.authSharedModule
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val otpModule = module {
    includes(authSharedModule, phoneNumberUpdateModule)
    factory { OTPRemoteDataSource(get()) }
    factory { OTPService(getProperty("BASE_URL"), getProperty("API_VERSION")) }
    factory <IOTPRepository>{ OTPRepository(get(), get(), get()) }
    factory { RequestOTPUseCase() }
    factory { VerifyOTPUseCase() }
    viewModel { OTPViewModel(get(), get(), get()) }
}