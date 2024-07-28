package com.haltec.silpusitron.feature.officertask.tasks.domain

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDateTime

interface IOfficerTasksRepository {
    fun getSubmittedLetters(
        filterStartDate: LocalDateTime?,
        filterEndDate: LocalDateTime?,
        filterLetterTypeId: Int?,
        searchKeyword: String?,
    ): Flow<PagingData<SubmittedLetter>>
}