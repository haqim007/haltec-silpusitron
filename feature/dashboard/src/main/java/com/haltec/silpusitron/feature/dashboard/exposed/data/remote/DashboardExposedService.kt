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
        districtId: String? = null,
        startDate: String? = null,
        endDate: String? = null
    ): DashboardResponse {
        val response = client.get {
            val parameters: MutableList<Pair<String, String>> = mutableListOf()

            if (districtId != null){
                parameters.add(Pair("kecamatan_id", districtId))
            }

            if (startDate != null && endDate != null){
                parameters.add(Pair("tanggal_awal", startDate))
                parameters.add(Pair("tanggal_akhir", endDate))
            }

            endpoint("landing_page", parameters)
        }

        checkOrThrowError(response)

        return response.body<DashboardResponse>()
    }

    suspend fun getNewsImages(): NewsImagesResponse {
        val response = client.get {
            endpoint("konten",)
        }

        checkOrThrowError(response)

        return response.body<NewsImagesResponse>()
    }
}