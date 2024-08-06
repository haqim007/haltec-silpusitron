package com.silpusitron.feature.dashboard.user.data.remote

import com.silpusitron.data.remote.base.KtorService
import com.silpusitron.feature.dashboard.common.data.remote.response.DashboardResponse
import com.silpusitron.feature.dashboard.exposed.data.remote.NewsImagesResponse
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.http.ParametersBuilder

internal class DashboardUserService(
    override val BASE_URL: String,
    override val API_VERSION: String
) : KtorService() {

    private val path = "dashboard"
    suspend fun getDashboard(token: String): DashboardResponse {
        val response = client.get {
            bearerAuth(token)
            endpoint(path)
        }

        checkOrThrowError(response)

        return response.body<DashboardResponse>()
    }

    suspend fun getNewsImages(token: String): NewsImagesResponse {
        val response = client.get {
            endpoint("konten-landing")
            bearerAuth(token)
        }

        checkOrThrowError(response)

        return response.body<NewsImagesResponse>()
    }
}