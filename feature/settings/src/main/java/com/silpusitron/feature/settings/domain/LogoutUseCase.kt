package com.silpusitron.feature.settings.domain

import com.silpusitron.data.mechanism.Resource
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class LogoutUseCase: KoinComponent {
    private val repository: ISettingsRepository by inject()

    suspend operator fun invoke(): Flow<Resource<String>>{
        return repository.logout()
    }
}