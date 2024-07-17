package com.haltec.silpusitron.feature.submission.data.remote

import com.haltec.silpusitron.data.mechanism.getResult

internal class SubmissionDocRemoteDatasource(
    private val service: SubmissionDocService
) {
    suspend fun getTemplate(id: Int, token: String) = getResult {
        service.getTemplate(id, token)
    }
}