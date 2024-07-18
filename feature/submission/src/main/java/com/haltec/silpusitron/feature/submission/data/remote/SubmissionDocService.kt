package com.haltec.silpusitron.feature.submission.data.remote

import com.haltec.silpusitron.data.remote.base.KtorService
import com.haltec.silpusitron.feature.submission.data.remote.request.SubmitSubmissionRequest
import com.haltec.silpusitron.feature.submission.data.remote.response.SubmitResponse
import com.haltec.silpusitron.feature.submission.data.remote.response.TemplateResponse
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

internal class SubmissionDocService(
    override val BASE_URL: String,
    override val API_VERSION: String
) : KtorService() {
    suspend fun getTemplate(
        id: Int,
        token: String
    ): TemplateResponse {
        val response = client.get {
            endpoint("template/$id")
            bearerAuth(token)
        }

        checkOrThrowError(response)

        return response.body<TemplateResponse>()
    }

    suspend fun submit(
        token: String,
        request: SubmitSubmissionRequest
    ): SubmitResponse {
        val response = client.post {
            endpoint("surat")
            bearerAuth(token)

            setBody(request)
        }

        checkOrThrowError(response)

        return response.body<SubmitResponse>()
    }
}