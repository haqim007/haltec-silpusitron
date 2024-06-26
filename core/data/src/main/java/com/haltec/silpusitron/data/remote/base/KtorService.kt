package com.haltec.silpusitron.data.remote.base

import android.util.Log
import com.haltec.silpusitron.data.util.CustomRequestException
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.cio.CIO
import io.ktor.client.engine.cio.CIOEngineConfig
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.path
import io.ktor.http.takeFrom
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

//private const val BASE_URL = "http://silpusitron.haltec.id/"
// "api/v1/"
abstract class KtorService{

    protected abstract val BASE_URL: String
    protected abstract val API_VERSION: String

    private fun HttpClientConfig<CIOEngineConfig>.basicClient() {
        install(Logging) {
            //logger = Logger.ANDROID
            logger = object : Logger {
                override fun log(message: String) {
                    Log.d(KtorService::class.simpleName, message)
                }
            }
            level = LogLevel.ALL
            sanitizeHeader { header -> header == HttpHeaders.Authorization }
        }

        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                useAlternativeNames = false
            })
        }

        install(HttpTimeout){
            socketTimeoutMillis = 3000L
            connectTimeoutMillis = 3000L
            requestTimeoutMillis = 3000L
        }
    }

    fun createClient(): HttpClient{
        return HttpClient(CIO){
            basicClient()
        }
    }




    fun HttpRequestBuilder.endpoint(path: String, type: ContentType = ContentType.Application.Json){
        url {
            takeFrom(BASE_URL)
            path("$API_VERSION$path")
            contentType(type)
        }
    }

    protected suspend fun checkOrThrowError(response: HttpResponse) {
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
    }


}
