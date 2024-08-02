package com.silpusitron.feature.officertask.tasks.data

import com.silpusitron.data.mechanism.getResult

internal class OfficerTasksRemoteDataSource(
    private val service: OfficerTasksService
) {
    suspend fun getSubmittedLetters(
        token: String,
        page: Int,
        search: String?,
        startDate: String?,
        endDate: String?,
        letterType: String?,
    ) = getResult {
        service.getSubmittedLetters(token, page, search, startDate, endDate, letterType)
    }
}