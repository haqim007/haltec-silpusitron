package com.silpusitron.feature.submission.form.data.remote

import com.silpusitron.data.mechanism.getResult
import com.silpusitron.feature.submission.form.data.remote.request.SubmitSubmissionRequest
import com.silpusitron.feature.submission.form.data.remote.response.SubmitResponse

internal class SubmissionDocRemoteDatasource(
    private val service: SubmissionDocService
) {
    suspend fun getTemplate(id: Int, token: String) = getResult {
        service.getTemplate(id, token)
    }

    suspend fun submit(token: String, request: SubmitSubmissionRequest) = getResult {
        service.submit(token, request)
    }
}