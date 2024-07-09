package com.haltec.silpusitron.feature.dashboard.exposed.data.remote

import com.haltec.silpusitron.data.remote.base.KtorService
import com.haltec.silpusitron.feature.dashboard.common.data.remote.response.DashboardResponse
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.http.ParametersBuilder

class DashboardExposedService(
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

    suspend fun getDashboard(
        districtId: Int? = null,
        startDate: String? = null,
        endDate: String? = null
    ): DashboardResponse {
        val response = client.get {
            val parametersBuilder = ParametersBuilder()

            if (startDate != null && endDate != null){
                parametersBuilder.append("tanggal_awal", startDate)
                parametersBuilder.append("tanggal_akhir", endDate)
            }

            if (districtId != null){
                endpoint("landing_page/$districtId", parametersBuilder)
            }
            else {
                endpoint("landing_page", parametersBuilder)
            }
        }

        checkOrThrowError(response)

        return response.body<DashboardResponse>()
    }
}