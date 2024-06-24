package com.haltec.silpusitron.feature.dashboard.data.remote

import com.haltec.silpusitron.data.mechanism.getResult

class DashboardRemoteDataSource(
    private val service: DashboardService
) {
    suspend fun getDashboard(token: String) = getResult {
        service.getDashboard(token)
    }
}