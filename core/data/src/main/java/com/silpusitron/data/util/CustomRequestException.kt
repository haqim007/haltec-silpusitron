package com.silpusitron.data.util

import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.json.JsonElement

class CustomRequestException (
    val dataJson: JsonElement? = null,
    val statusCode: HttpStatusCode,
    val errorMessage: String? = null
) : Exception("Client Request Exception: ${statusCode.value} - ${errorMessage ?: ""}")