package com.silpusitron.feature.auth.common.data.remote

import com.silpusitron.data.remote.base.KtorService
import com.silpusitron.data.util.CustomRequestException
import com.silpusitron.feature.auth.login.data.remote.LoginRequest
import com.silpusitron.feature.auth.login.data.remote.LoginResponse
import com.silpusitron.feature.auth.otp.data.remote.RequestOTPResponse
import com.silpusitron.feature.auth.otp.data.remote.VerifyOTPRequest
import com.silpusitron.feature.auth.otp.data.remote.VerifyOTPResponse
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class AuthService(
    override val BASE_URL: String,
    override val API_VERSION: String
) : KtorService(){
    private val path = "users"
    suspend fun login(request: LoginRequest): LoginResponse {
        val response = client.post {
            endpoint("$path/token")
            setBody(request)
        }

        checkOrThrowError(response)

        return response.body<LoginResponse>()
    }
}