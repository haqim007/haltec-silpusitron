package com.silpusitron.feature.dashboard.user.di

import com.silpusitron.feature.dashboard.user.domain.usecase.GetDashboardUserUseCase
import com.silpusitron.feature.dashboard.user.ui.DashboardUserViewModel
import com.silpusitron.feature.dashboard.user.data.remote.DashboardUserRemoteDataSource
import com.silpusitron.feature.dashboard.user.data.remote.DashboardUserService
import com.silpusitron.feature.dashboard.user.data.repository.DashboardUserRepository
import com.silpusitron.feature.dashboard.user.domain.repository.IDashboardUserRepository
import com.silpusitron.feature.dashboard.user.domain.usecase.GetNewsImagesUseCase
import com.silpusitron.shared.auth.di.authSharedModule
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val dashboardUserModule = module {
    includes(authSharedModule)
    factory { DashboardUserService(getProperty("BASE_URL"), getProperty("API_VERSION")) }
    factory { DashboardUserRemoteDataSource(get()) }
    factory<IDashboardUserRepository> { DashboardUserRepository(get(), get(), get()) }
    factory { GetDashboardUserUseCase() }
    factory { GetNewsImagesUseCase() }
    viewModel { DashboardUserViewModel(get(), get()) }
}