package com.silpusitron.feature.dashboard.user.data.remote

import com.silpusitron.data.mechanism.getResult

internal class DashboardUserRemoteDataSource(
    private val service: DashboardUserService
) {
    suspend fun getDashboard(token: String) = getResult {
        service.getDashboard(token)
    }
}