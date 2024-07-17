package com.haltec.silpusitron.user.profile.data.remote

import com.haltec.silpusitron.data.remote.base.KtorService
import com.haltec.silpusitron.user.profile.data.remote.request.ProfileRequest
import com.haltec.silpusitron.user.profile.data.remote.response.SubmitProfileResponse
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.put
import io.ktor.client.request.setBody


class ProfileService(
    override val BASE_URL: String,
    override val API_VERSION: String
) : KtorService() {

    suspend fun submitProfile(token: String, data: ProfileRequest): SubmitProfileResponse{
        val response = client.put{
            endpoint("users/profile")
            bearerAuth(token)
            setBody(data)
        }

        checkOrThrowError(response)

        return response.body()
    }
}