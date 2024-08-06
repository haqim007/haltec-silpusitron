package com.silpusitron.feature.settings.data

import com.silpusitron.data.remote.base.KtorService
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get

class SettingsService(
    override val BASE_URL: String,
    override val API_VERSION: String
) : KtorService() {
    suspend fun logout(token: String): LogoutResponse{
        val response = client.get {
            endpoint("logout")
            bearerAuth(token)
        }

        checkOrThrowError(response)

        return response.body()
    }
}