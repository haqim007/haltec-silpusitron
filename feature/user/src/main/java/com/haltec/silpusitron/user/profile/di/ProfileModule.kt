package com.haltec.silpusitron.user.profile.di

import com.haltec.silpusitron.shared.auth.di.authSharedModule
import com.haltec.silpusitron.shared.formprofile.di.formProfileModule
import com.haltec.silpusitron.shared.formprofile.domain.usecase.ValidateAllInputUseCase
import com.haltec.silpusitron.user.profile.data.remote.ProfileRemoteDataSource
import com.haltec.silpusitron.user.profile.data.remote.ProfileService
import com.haltec.silpusitron.user.profile.data.repository.ProfileRepository
import com.haltec.silpusitron.user.profile.domain.IProfileRepository
import com.haltec.silpusitron.user.profile.domain.usecase.SubmitProfileUseCase
import com.haltec.silpusitron.user.profile.ui.ProfileDataViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val profileModule = module {
    includes(authSharedModule, formProfileModule)
    factory { ProfileService(getProperty("BASE_URL"), getProperty("API_VERSION")) }
    factory { ProfileRemoteDataSource(get()) }
    factory<IProfileRepository> { ProfileRepository(get(), get(), get()) }
    factory { SubmitProfileUseCase() }
    factory { ValidateAllInputUseCase() }
    viewModel {
        ProfileDataViewModel(
            get(), get(), get(),
            get(), get(), get(),
            get(), get(), get(),
            get(), get(), get()
        )
    }
}