package com.silpusitron.shared.district.data.remote

import com.silpusitron.data.remote.base.KtorService
import com.silpusitron.shared.district.data.remote.response.DistrictsResponse
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get

class DistrictService(
    override val BASE_URL: String,
    override val API_VERSION: String
) : KtorService() {
    suspend fun getDistricts(): DistrictsResponse {
        val response = client.get{
            endpoint("kecamatan")
        }

        checkOrThrowError(response)

        return response.body()
    }
}