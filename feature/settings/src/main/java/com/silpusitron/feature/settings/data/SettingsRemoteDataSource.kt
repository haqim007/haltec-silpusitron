package com.silpusitron.feature.settings.data

import com.silpusitron.data.mechanism.getResult

class SettingsRemoteDataSource(
    private val service: SettingsService
) {
    suspend fun logout(token: String) = getResult {
        service.logout(token)
    }
}