package com.haltec.silpusitron.feature.officertask.tasks.data

import com.haltec.silpusitron.data.mechanism.getResult

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