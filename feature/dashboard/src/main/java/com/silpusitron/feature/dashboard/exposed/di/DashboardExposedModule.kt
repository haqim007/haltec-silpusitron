package com.silpusitron.feature.dashboard.exposed.di

import com.silpusitron.feature.dashboard.exposed.data.remote.DashboardExposedRemoteDataSource
import com.silpusitron.feature.dashboard.exposed.data.remote.DashboardExposedService
import com.silpusitron.feature.dashboard.exposed.data.repository.DashboardExposedRepository
import com.silpusitron.feature.dashboard.exposed.domain.repository.IDashboardExposedRepository
import com.silpusitron.feature.dashboard.exposed.domain.usecase.GetDashboardExposedUseCase
import com.silpusitron.feature.dashboard.exposed.domain.usecase.GetNewsImagesUseCase
import com.silpusitron.feature.dashboard.exposed.ui.DashboardExposedViewModel
import com.silpusitron.shared.district.di.districtModule
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val dashboardExposedModule = module {
    factory { DashboardExposedService(getProperty("BASE_URL"), getProperty("API_VERSION")) }
    factory { DashboardExposedRemoteDataSource(get()) }
    factory<IDashboardExposedRepository> { DashboardExposedRepository(get(), get()) }
    includes(districtModule)
    factory { GetDashboardExposedUseCase() }
    factory { GetNewsImagesUseCase() }
    viewModel { DashboardExposedViewModel(get(), get(), get()) }
}