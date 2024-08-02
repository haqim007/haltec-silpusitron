package com.silpusitron.feature.dashboard.user.domain.repository

import com.silpusitron.data.mechanism.Resource
import com.silpusitron.feature.dashboard.common.domain.model.DashboardData
import kotlinx.coroutines.flow.Flow

interface IDashboardUserRepository {
    fun getData(): Flow<Resource<List<DashboardData>>>
}