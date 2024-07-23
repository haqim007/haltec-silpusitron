package com.haltec.silpusitron.user.profileold.di

import com.haltec.silpusitron.shared.auth.di.authSharedModule
import com.haltec.silpusitron.shared.formprofile.di.formProfileModule
import com.haltec.silpusitron.shared.formprofile.domain.usecase.ValidateAllInputUseCase
import com.haltec.silpusitron.user.profileold.data.remote.ProfileRemoteDataSource
import com.haltec.silpusitron.user.profileold.data.remote.ProfileService
import com.haltec.silpusitron.user.profileold.data.repository.ProfileRepository
import com.haltec.silpusitron.user.profileold.domain.IProfileRepository
import com.haltec.silpusitron.user.profileold.domain.usecase.SubmitProfileUseCase
import com.haltec.silpusitron.user.profileold.ui.ProfileDataViewModel
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