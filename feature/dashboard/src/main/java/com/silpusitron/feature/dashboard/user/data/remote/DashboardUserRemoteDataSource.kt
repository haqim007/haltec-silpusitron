package com.silpusitron.feature.dashboard.user.data.remote

import com.silpusitron.data.mechanism.getResult
import com.silpusitron.feature.dashboard.exposed.data.remote.NewsImagesResponse

internal class DashboardUserRemoteDataSource(
    private val service: DashboardUserService
) {
    suspend fun getDashboard(token: String) = getResult {
        service.getDashboard(token)
    }

    suspend fun getNewsImages(token: String) = getResult {
        service.getNewsImages(token)
    }
}