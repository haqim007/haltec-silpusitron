package com.silpusitron.feature.auth.otp.data.remote

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

class OTPService(
    override val BASE_URL: String,
    override val API_VERSION: String
) : KtorService(){
    private val path = "users"

    suspend fun verifyOTP(request: VerifyOTPRequest, token: String): VerifyOTPResponse{
        val response = client.post {
            bearerAuth(token)
            endpoint("$path/otp")
            setBody(request)
        }

        checkOrThrowError(response)

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
                dataJson = json,
                statusCode = response.status,
                errorMessage = message
            )
        }

        return response.body<RequestOTPResponse>()
    }
}