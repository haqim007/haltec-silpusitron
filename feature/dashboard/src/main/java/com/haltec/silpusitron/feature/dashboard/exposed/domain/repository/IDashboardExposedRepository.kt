package com.haltec.silpusitron.feature.dashboard.exposed.domain.repository

import com.haltec.silpusitron.data.mechanism.Resource
import com.haltec.silpusitron.feature.dashboard.common.domain.model.DashboardData
import kotlinx.coroutines.flow.Flow

interface IDashboardExposedRepository {
    fun getData(
        districtId: String? = null,
        startDate: String? = null,
        endDate: String? = null
    ): Flow<Resource<List<DashboardData>>>
}