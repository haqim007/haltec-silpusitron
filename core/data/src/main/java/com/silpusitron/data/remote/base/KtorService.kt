package com.silpusitron.data.remote.base

import android.util.Log
import com.silpusitron.data.di.ktorHttpClient
import com.silpusitron.data.util.CustomRequestException
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
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

abstract class KtorService: KoinComponent{

    protected abstract val BASE_URL: String
    protected abstract val API_VERSION: String

    protected open val client: HttpClient by inject(ktorHttpClient)

    fun HttpRequestBuilder.endpoint(
        path: String,
        parametersList: List<Pair<String, String>> = listOf(), // Pair<Name, Value>
        type: ContentType = ContentType.Application.Json
    ){
        url {
            takeFrom(BASE_URL)
            path("$API_VERSION$path")
            parametersList.forEach { param ->
                parameters.append(param.first, param.second)
            }
            contentType(type)
        }
    }

    protected suspend fun checkOrThrowError(response: HttpResponse) {
        // Parse JSON string into a dynamic structure (JsonElement)
        val json = Json.parseToJsonElement(response.bodyAsText())
        val errors = json.jsonObject["errors"]?.jsonObject
        val message = json.jsonObject["message"]?.jsonPrimitive?.content
        if (response.status != HttpStatusCode.OK && (message != null || errors != null)) {
            throw CustomRequestException(
                dataJson = json,
                statusCode = response.status,
                errorMessage = message
            )
        }
    }


}
