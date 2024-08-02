package com.silpusitron.feature.auth.common.domain

import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class CheckSessionUseCase: KoinComponent {
    private val repository: IAuthRepository by inject<IAuthRepository>()
    suspend operator fun invoke(): Flow<Boolean> {
        return repository.checkSession()
    }
}