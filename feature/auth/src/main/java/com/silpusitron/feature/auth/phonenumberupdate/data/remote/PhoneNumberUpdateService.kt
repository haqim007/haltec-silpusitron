package com.silpusitron.feature.auth.phonenumberupdate.data.remote

import com.silpusitron.data.remote.base.KtorService
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.put
import io.ktor.client.request.setBody

class PhoneNumberUpdateService(
    override val BASE_URL: String,
    override val API_VERSION: String
) : KtorService() {
    suspend fun update(
        token: String,
        data: PhoneNumberUpdateRequest
    ): PhoneNumberUpdateResponse{
        val response = client.put{
            endpoint("users/update")
            bearerAuth(token)
            setBody(data)
        }

        checkOrThrowError(response)

        return response.body()
    }
}
