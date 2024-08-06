package com.silpusitron.feature.auth.phonenumberupdate.di

import com.silpusitron.feature.auth.phonenumberupdate.data.PhoneNumberUpdateRepository
import com.silpusitron.feature.auth.phonenumberupdate.data.remote.PhoneNumberUpdateRemoteDataSource
import com.silpusitron.feature.auth.phonenumberupdate.data.remote.PhoneNumberUpdateService
import com.silpusitron.feature.auth.phonenumberupdate.domain.repository.IPhoneNumberRepository
import com.silpusitron.feature.auth.phonenumberupdate.domain.usecase.PhoneNumberUpdateUseCase
import com.silpusitron.feature.auth.phonenumberupdate.ui.PhoneNumberUpdateViewModel
import com.silpusitron.shared.auth.di.authSharedModule
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val phoneNumberUpdateModule = module {
    includes(authSharedModule)
    factory { PhoneNumberUpdateRemoteDataSource(get()) }
    factory { PhoneNumberUpdateService(getProperty("BASE_URL"), getProperty("API_VERSION")) }
    factory <IPhoneNumberRepository>{ PhoneNumberUpdateRepository(get(), get()) }
    factory { PhoneNumberUpdateUseCase() }
    viewModel { PhoneNumberUpdateViewModel(get()) }
}