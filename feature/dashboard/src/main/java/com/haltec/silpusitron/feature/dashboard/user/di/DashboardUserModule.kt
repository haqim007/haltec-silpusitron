package com.haltec.silpusitron.feature.dashboard.user.di

import com.haltec.silpusitron.feature.dashboard.common.domain.usecase.GetDashboardUserUseCase
import com.haltec.silpusitron.feature.dashboard.user.ui.DashboardUserViewModel
import com.haltec.silpusitron.feature.dashboard.user.data.remote.DashboardUserRemoteDataSource
import com.haltec.silpusitron.feature.dashboard.user.data.remote.DashboardUserService
import com.haltec.silpusitron.feature.dashboard.user.data.repository.DashboardUserRepository
import com.haltec.silpusitron.feature.dashboard.user.domain.repository.IDashboardUserRepository
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

internal val dashboardUserModule = module {
    factory { DashboardUserService(getProperty("BASE_URL"), getProperty("API_VERSION")) }
    factory { DashboardUserRemoteDataSource(get()) }
    factory<IDashboardUserRepository> { DashboardUserRepository(get(), get(), get()) }
    factory { DashboardUserService(get(), get()) }
    factory { GetDashboardUserUseCase() }
    viewModel { DashboardUserViewModel(get()) }
}