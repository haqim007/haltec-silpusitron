package com.haltec.silpusitron.user.account.domain

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class LogoutUseCase: KoinComponent {
    private val repository: IAccountRepository by inject()

    suspend operator fun invoke(){
        repository.logout()
    }
}