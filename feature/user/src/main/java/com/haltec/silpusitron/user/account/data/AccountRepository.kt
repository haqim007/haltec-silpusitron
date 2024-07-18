package com.haltec.silpusitron.user.account.data

import com.haltec.silpusitron.common.util.DispatcherProvider
import com.haltec.silpusitron.shared.auth.preference.AuthPreference
import com.haltec.silpusitron.user.account.domain.IAccountRepository
import kotlinx.coroutines.withContext

class AccountRepository(
    private val authPreference: AuthPreference,
    private val dispatcherProvider: DispatcherProvider
): IAccountRepository {
    override suspend fun logout() {
        withContext(dispatcherProvider.io){
            authPreference.resetAuth()
        }
    }
}