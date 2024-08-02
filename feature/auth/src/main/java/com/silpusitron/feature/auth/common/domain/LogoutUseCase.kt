package com.silpusitron.feature.auth.common.domain

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.java.KoinJavaComponent.inject

class LogoutUseCase: KoinComponent {
    private val repository: IAuthRepository by inject<IAuthRepository>()
    suspend operator fun invoke(){
        repository.logout()
    }
}