package com.silpusitron.feature.settings.domain

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class LogoutUseCase: KoinComponent {
    private val repository: ISettingsRepository by inject()

    suspend operator fun invoke(){
        repository.logout()
    }
}