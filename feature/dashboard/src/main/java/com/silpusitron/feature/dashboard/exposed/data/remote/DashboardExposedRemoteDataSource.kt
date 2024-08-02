package com.silpusitron.feature.dashboard.exposed.data.remote

import com.silpusitron.data.mechanism.getResult

class DashboardExposedRemoteDataSource(
    private val service: DashboardExposedService
) {
    suspend fun getDashboard(
        districtId: String? = null,
        startDate: String? = null,
        endDate: String? = null
    ) = getResult {
        service.getDashboard(districtId, startDate, endDate)
    }

    suspend fun getNewsImages() = getResult {
        service.getNewsImages()
    }
}