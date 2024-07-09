package com.haltec.silpusitron.feature.dashboard.exposed.data.remote

import com.haltec.silpusitron.data.mechanism.getResult

class DashboardExposedRemoteDataSource(
    private val service: DashboardExposedService
) {
    suspend fun getDashboard() = getResult {
        service.getDashboard()
    }
}