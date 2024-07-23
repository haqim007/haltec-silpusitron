package com.haltec.silpusitron.feature.dashboard.user.data.remote

import com.haltec.silpusitron.data.mechanism.getResult

internal class DashboardUserRemoteDataSource(
    private val service: DashboardUserService
) {
    suspend fun getDashboard(token: String) = getResult {
        service.getDashboard(token)
    }
}