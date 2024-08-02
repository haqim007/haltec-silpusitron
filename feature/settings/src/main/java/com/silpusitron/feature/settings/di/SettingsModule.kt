package com.silpusitron.feature.settings.di

import com.silpusitron.feature.settings.data.SettingsRepository
import com.silpusitron.feature.settings.domain.ISettingsRepository
import com.silpusitron.feature.settings.domain.LogoutUseCase
import com.silpusitron.feature.settings.ui.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val settingsModule = module {
    factory<ISettingsRepository> { SettingsRepository(get(), get()) }
    factory { LogoutUseCase() }
    viewModel { SettingsViewModel(get()) }
}