package com.haltec.silpusitron.feature.dashboard.exposed.domain.repository

import com.haltec.silpusitron.data.mechanism.Resource
import com.haltec.silpusitron.feature.dashboard.common.domain.model.DashboardData
import kotlinx.coroutines.flow.Flow

interface IDashboardExposedRepository {
    fun getData(): Flow<Resource<List<DashboardData>>>
}