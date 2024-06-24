package com.haltec.silpusitron.feature.dashboard.domain.repository

import com.haltec.silpusitron.data.mechanism.Resource
import com.haltec.silpusitron.feature.dashboard.domain.model.DashboardData
import kotlinx.coroutines.flow.Flow

interface IDashboardRepository {
    fun getData(): Flow<Resource<DashboardData>>
}