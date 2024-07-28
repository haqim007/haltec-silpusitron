package com.haltec.silpusitron.shared.lettertype.data

import com.haltec.silpusitron.data.remote.base.KtorService
import io.ktor.client.call.body
import io.ktor.client.request.get

internal class LetterTypesService (
    override val BASE_URL: String,
    override val API_VERSION: String
) : KtorService() {

    suspend fun getLetterTypes(): LetterTypesResponse {
        val response = client.get { endpoint("jenis-surat") }
        checkOrThrowError(response)
        return response.body<LetterTypesResponse>()
    }

}