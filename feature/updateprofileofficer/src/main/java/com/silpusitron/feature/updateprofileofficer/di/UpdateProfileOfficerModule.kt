package com.silpusitron.feature.updateprofileofficer.di

import com.silpusitron.common.di.commonModule
import com.silpusitron.shared.auth.di.authSharedModule
import com.silpusitron.feature.updateprofileofficer.data.remote.ProfileOfficerRemoteDataSource
import com.silpusitron.feature.updateprofileofficer.data.remote.ProfileOfficerService
import com.silpusitron.feature.updateprofileofficer.data.repository.UpdateProfileOfficerRepository
import com.silpusitron.feature.updateprofileofficer.domain.GetProfileOfficerUseCase
import com.silpusitron.feature.updateprofileofficer.domain.IUpdateProfileOfficerRepository
import com.silpusitron.feature.updateprofileofficer.domain.SubmitProfileOfficerUseCase
import com.silpusitron.feature.updateprofileofficer.ui.UpdateProfileOfficerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val updateProfileOfficerModule = module {
    includes(authSharedModule, commonModule)
    factory { ProfileOfficerService(getProperty("BASE_URL"), getProperty("API_VERSION")) }
    factory { ProfileOfficerRemoteDataSource(get()) }
    factory<IUpdateProfileOfficerRepository> { UpdateProfileOfficerRepository(get(), get(), get()) }
    factory { GetProfileOfficerUseCase() }
    factory { SubmitProfileOfficerUseCase() }
    viewModel { UpdateProfileOfficerViewModel(get(), get()) }
}