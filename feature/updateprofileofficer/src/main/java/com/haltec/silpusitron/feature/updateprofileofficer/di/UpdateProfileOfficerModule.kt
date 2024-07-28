package com.haltec.silpusitron.feature.updateprofileofficer.di

import com.haltec.silpusitron.common.di.commonModule
import com.haltec.silpusitron.shared.auth.di.authSharedModule
import com.haltec.silpusitron.feature.updateprofileofficer.data.remote.ProfileOfficerRemoteDataSource
import com.haltec.silpusitron.feature.updateprofileofficer.data.remote.ProfileOfficerService
import com.haltec.silpusitron.feature.updateprofileofficer.data.repository.UpdateProfileOfficerRepository
import com.haltec.silpusitron.feature.updateprofileofficer.domain.GetProfileOfficerUseCase
import com.haltec.silpusitron.feature.updateprofileofficer.domain.IUpdateProfileOfficerRepository
import com.haltec.silpusitron.feature.updateprofileofficer.domain.SubmitProfileOfficerUseCase
import com.haltec.silpusitron.feature.updateprofileofficer.ui.UpdateProfileOfficerViewModel
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