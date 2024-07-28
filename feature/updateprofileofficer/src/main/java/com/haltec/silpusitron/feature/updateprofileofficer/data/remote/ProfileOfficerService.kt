package com.haltec.silpusitron.feature.updateprofileofficer.data.remote

import com.haltec.silpusitron.data.remote.base.KtorService
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.request.put
import io.ktor.client.request.setBody

internal class ProfileOfficerService(
    override val BASE_URL: String,
    override val API_VERSION: String
) : KtorService() {
    suspend fun getProfile(token: String): ProfileOfficerResponse {
        val response = client.get{
            endpoint("users/profile")
            bearerAuth(token)
        }

        checkOrThrowError(response)

        return response.body()
    }

    suspend fun submitProfile(token: String, data: ProfileOfficerRequest): SubmitProfileOfficerResponse {
        val response = client.put{
            endpoint("users/profile")
            bearerAuth(token)
            setBody(data)
        }

        checkOrThrowError(response)

        return response.body()
    }
}