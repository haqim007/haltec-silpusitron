package com.silpusitron.feature.submission.form.data.remote

import com.silpusitron.data.mechanism.getResult
import com.silpusitron.feature.submission.form.data.remote.request.SubmitSubmissionRequest
import com.silpusitron.feature.submission.form.data.remote.request.SubmitUpdateSubmissionRequest
import com.silpusitron.feature.submission.form.data.remote.response.SubmitResponse
import org.koin.androidx.compose.get

internal class SubmissionDocRemoteDatasource(
    private val service: SubmissionDocService
) {
    suspend fun getTemplate(id: Int, token: String) = getResult {
        service.getTemplate(id, token)
    }

    suspend fun submit(token: String, request: SubmitSubmissionRequest) = getResult {
        service.submit(token, request)
    }

    suspend fun getDraftSubmission(token: String,submissionId: Int) = getResult {
        service.getDraftSubmission(submissionId, token)
    }

    suspend fun submitUpdate(
        token: String,
        id: Int,
        request: SubmitUpdateSubmissionRequest
    ) = getResult {
        service.submitUpdate(token, id, request)
    }
}