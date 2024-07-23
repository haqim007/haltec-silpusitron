package com.haltec.silpusitron.feature.submission.history.data.remote

import com.haltec.silpusitron.data.mechanism.getResult
import com.haltec.silpusitron.feature.submission.history.data.remote.response.LetterTypesResponse

class SubmissionHistoryRemoteDataSource(
    private val service: SubmissionHistoryService
) {
    suspend fun getHistories(
        token: String,
        page: Int,
        search: String?,
        startDate: String?,
        endDate: String?,
        letterType: String?,
        letterStatus: String?,
    ) = getResult {
        service.getHistories(token, page, search, startDate, endDate, letterType, letterStatus)
    }

    suspend fun getLetterTypes(): Result<LetterTypesResponse> = getResult {
        service.getLetterTypes()
    }
}