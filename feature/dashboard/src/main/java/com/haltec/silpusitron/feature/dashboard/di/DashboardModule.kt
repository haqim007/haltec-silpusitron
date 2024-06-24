package com.haltec.silpusitron.feature.dashboard.di

import com.haltec.silpusitron.feature.dashboard.data.remote.DashboardRemoteDataSource
import com.haltec.silpusitron.feature.dashboard.data.remote.DashboardService
import com.haltec.silpusitron.feature.dashboard.data.repository.DashboardRepository
import com.haltec.silpusitron.feature.dashboard.domain.repository.IDashboardRepository
import com.haltec.silpusitron.feature.dashboard.domain.usecase.GetDashboardDataUseCase
import com.haltec.silpusitron.feature.dashboard.ui.DashboardViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val dashboardModule = module {
    factory { DashboardService(getProperty("BASE_URL"), getProperty("API_VERSION")) }
    factory { DashboardRemoteDataSource(get()) }
    factory<IDashboardRepository> { DashboardRepository(get(), get(), get()) }
    factory { GetDashboardDataUseCase() }
    viewModel { DashboardViewModel(get()) }
}