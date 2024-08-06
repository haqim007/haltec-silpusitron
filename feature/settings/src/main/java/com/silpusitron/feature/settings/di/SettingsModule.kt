package com.silpusitron.feature.settings.di

import com.silpusitron.feature.settings.data.SettingsRemoteDataSource
import com.silpusitron.feature.settings.data.SettingsRepository
import com.silpusitron.feature.settings.data.SettingsService
import com.silpusitron.feature.settings.domain.ISettingsRepository
import com.silpusitron.feature.settings.domain.LogoutUseCase
import com.silpusitron.feature.settings.ui.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val settingsModule = module {
    factory { SettingsService(getProperty("BASE_URL"), getProperty("API_VERSION")) }
    factory { SettingsRemoteDataSource(get()) }
    factory<ISettingsRepository> { SettingsRepository(get(), get(), get()) }
    factory { LogoutUseCase() }
    viewModel { SettingsViewModel(get()) }
}