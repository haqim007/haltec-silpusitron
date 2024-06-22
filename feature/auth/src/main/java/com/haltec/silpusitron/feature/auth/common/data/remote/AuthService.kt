package com.haltec.silpusitron.feature.auth.common.data.remote

import com.haltec.silpusitron.data.remote.base.KtorService
import com.haltec.silpusitron.data.util.CustomRequestException
import com.haltec.silpusitron.feature.auth.login.data.remote.LoginRequest
import com.haltec.silpusitron.feature.auth.login.data.remote.LoginResponse
import com.haltec.silpusitron.feature.auth.otp.data.remote.RequestOTPResponse
import com.haltec.silpusitron.feature.auth.otp.data.remote.VerifyOTPRequest
import com.haltec.silpusitron.feature.auth.otp.data.remote.VerifyOTPResponse
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class AuthService(
    override val BASE_URL: String,
    override val API_VERSION: String
) : KtorService(){
    private val client by lazy { createClient() }
    private val path = "users"
    suspend fun login(request: LoginRequest): LoginResponse {
        val response = client.post {
            endpoint("$path/token")
            setBody(request)
        }

        // Parse JSON string into a dynamic structure (JsonElement)
        val json = Json.parseToJsonElement(response.bodyAsText())
        val errors = json.jsonObject["errors"]?.jsonPrimitive?.content
        val message = json.jsonObject["message"]?.jsonPrimitive?.content
        if (response.status != HttpStatusCode.OK && (message != null || errors != null)) {
            throw CustomRequestException(
                dataJson = errors,
                statusCode = response.status,
                errorMessage = message
            )
        }

        return response.body<LoginResponse>()
    }

    suspend fun verifyOTP(request: VerifyOTPRequest, token: String): VerifyOTPResponse{
        val response = client.post {
            bearerAuth(token)
            endpoint("$path/otp")
            setBody(request)
        }

        // Parse JSON string into a dynamic structure (JsonElement)
        val json = Json.parseToJsonElement(response.bodyAsText())
        val errors = json.jsonObject["errors"]?.jsonPrimitive?.content
        val message = json.jsonObject["message"]?.jsonPrimitive?.content
        if (response.status != HttpStatusCode.OK && (message != null || errors != null)) {
            throw CustomRequestException(
                dataJson = errors,
                statusCode = response.status,
                errorMessage = message
            )
        }

        return response.body<VerifyOTPResponse>()
    }

    suspend fun requestOTP(token: String): RequestOTPResponse {
        val response = client.get {
            endpoint("$path/otp")
            bearerAuth(token)
        }

        // Parse JSON string into a dynamic structure (JsonElement)
        val json = Json.parseToJsonElement(response.bodyAsText())
        val errors = json.jsonObject["errors"]?.jsonPrimitive?.content
        val message = json.jsonObject["message"]?.jsonPrimitive?.content
        if (response.status != HttpStatusCode.OK && (message != null || errors != null)) {
            throw CustomRequestException(
                dataJson = errors,
                statusCode = response.status,
                errorMessage = message
            )
        }

        return response.body<RequestOTPResponse>()
    }
}