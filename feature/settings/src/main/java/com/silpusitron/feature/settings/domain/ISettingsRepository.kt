package com.silpusitron.feature.settings.domain

import com.silpusitron.data.mechanism.Resource
import kotlinx.coroutines.flow.Flow

interface ISettingsRepository {
    suspend fun logout(): Flow<Resource<String>>
}