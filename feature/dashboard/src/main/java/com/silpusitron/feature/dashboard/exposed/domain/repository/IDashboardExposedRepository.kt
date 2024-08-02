package com.silpusitron.feature.dashboard.exposed.domain.repository

import com.silpusitron.data.mechanism.Resource
import com.silpusitron.feature.dashboard.common.domain.model.DashboardData
import com.silpusitron.feature.dashboard.exposed.domain.model.NewsImage
import kotlinx.coroutines.flow.Flow

interface IDashboardExposedRepository {
    fun getData(
        districtId: String? = null,
        startDate: String? = null,
        endDate: String? = null
    ): Flow<Resource<List<DashboardData>>>

    fun getNewsimages(): Flow<Resource<List<NewsImage>>>
}