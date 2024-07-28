package com.haltec.silpusitron.feature.settings.data

import com.haltec.silpusitron.common.util.DispatcherProvider
import com.haltec.silpusitron.shared.auth.preference.AuthPreference
import com.haltec.silpusitron.feature.settings.domain.ISettingsRepository
import kotlinx.coroutines.withContext

class SettingsRepository(
    private val authPreference: AuthPreference,
    private val dispatcherProvider: DispatcherProvider
): ISettingsRepository {
    override suspend fun logout() {
        withContext(dispatcherProvider.io){
            authPreference.resetAuth()
        }
    }
}