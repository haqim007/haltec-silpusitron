package com.silpusitron.feature.settings.data

import com.silpusitron.common.util.DispatcherProvider
import com.silpusitron.shared.auth.preference.AuthPreference
import com.silpusitron.feature.settings.domain.ISettingsRepository
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