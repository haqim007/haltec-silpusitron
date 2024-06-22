package com.haltec.silpusitron.data.util

import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode

class CustomRequestException (
    val dataJson: String? = null,
    val statusCode: HttpStatusCode,
    val errorMessage: String? = null
) : Exception("Client Request Exception: ${statusCode.value} - ${errorMessage ?: ""}")