package com.haltec.silpusitron.feature.dashboard.exposed.di

import com.haltec.silpusitron.feature.dashboard.exposed.data.remote.DashboardExposedRemoteDataSource
import com.haltec.silpusitron.feature.dashboard.exposed.data.remote.DashboardExposedService
import com.haltec.silpusitron.feature.dashboard.exposed.data.repository.DashboardExposedRepository
import com.haltec.silpusitron.feature.dashboard.exposed.domain.repository.IDashboardExposedRepository
import com.haltec.silpusitron.feature.dashboard.exposed.domain.usecase.GetDashboardExposedUseCase
import com.haltec.silpusitron.feature.dashboard.exposed.ui.DashboardExposedViewModel
import com.haltec.silpusitron.shared.district.di.districtModule
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

internal val dashboardExposedModule = module {
    factory { DashboardExposedService(getProperty("BASE_URL"), getProperty("API_VERSION")) }
    factory { DashboardExposedRemoteDataSource(get()) }
    factory<IDashboardExposedRepository> { DashboardExposedRepository(get(), get()) }
    includes(districtModule)
    factory { GetDashboardExposedUseCase() }
    viewModel { DashboardExposedViewModel(get(), get()) }
}