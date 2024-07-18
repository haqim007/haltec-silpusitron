package com.haltec.silpusitron.feature.dashboard.user.domain.repository

import com.haltec.silpusitron.data.mechanism.Resource
import com.haltec.silpusitron.feature.dashboard.common.domain.model.DashboardData
import kotlinx.coroutines.flow.Flow

interface IDashboardUserRepository {
    fun getData(): Flow<Resource<List<DashboardData>>>
}