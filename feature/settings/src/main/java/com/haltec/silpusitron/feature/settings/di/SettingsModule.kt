package com.haltec.silpusitron.feature.settings.di

import com.haltec.silpusitron.feature.settings.data.SettingsRepository
import com.haltec.silpusitron.feature.settings.domain.ISettingsRepository
import com.haltec.silpusitron.feature.settings.domain.LogoutUseCase
import com.haltec.silpusitron.feature.settings.ui.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val settingsModule = module {
    factory<ISettingsRepository> { SettingsRepository(get(), get()) }
    factory { LogoutUseCase() }
    viewModel { SettingsViewModel(get()) }
}