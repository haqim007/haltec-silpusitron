package com.haltec.silpusitron.feature.dashboard.data.remote

import com.haltec.silpusitron.data.remote.base.KtorService
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get

class DashboardService(
    override val BASE_URL: String,
    override val API_VERSION: String
) : KtorService() {

    private val client by lazy { createClient() }
    private val path = "dashboard"
    suspend fun getDashboard(token: String): DashboardResponse{
        val response = client.get {
            bearerAuth(token)
            endpoint(path)
        }

        checkOrThrowError(response)

        return response.body<DashboardResponse>()
    }
}