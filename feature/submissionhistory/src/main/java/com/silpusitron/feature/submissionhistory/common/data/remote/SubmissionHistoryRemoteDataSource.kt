package com.silpusitron.feature.submissionhistory.common.data.remote

import com.silpusitron.data.mechanism.getResult
import com.silpusitron.feature.submissionhistory.common.data.remote.response.LetterStatusesResponse

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

    suspend fun getLetterStatuses(token: String): Result<LetterStatusesResponse> = getResult {
        service.getLetterStatuses(token)
    }
}