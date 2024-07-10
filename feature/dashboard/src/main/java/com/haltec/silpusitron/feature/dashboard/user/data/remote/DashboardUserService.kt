package com.haltec.silpusitron.feature.dashboard.user.data.remote

import com.haltec.silpusitron.data.remote.base.KtorService
import com.haltec.silpusitron.feature.dashboard.common.data.remote.response.DashboardResponse
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.http.ParametersBuilder

class DashboardUserService(
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
}