package com.haltec.silpusitron.feature.officertask.tasks.domain

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDateTime
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetSubmittedLettersUseCase: KoinComponent {

    private val repository: IOfficerTasksRepository by inject()

    operator fun invoke(
        filterStartDate: LocalDateTime?,
        filterEndDate: LocalDateTime?,
        filterLetterTypeId: Int?,
        searchKeyword: String?,
    ): Flow<PagingData<SubmittedLetter>> {
        return repository.getSubmittedLetters(
            filterStartDate,
            filterEndDate,
            filterLetterTypeId,
            searchKeyword,
        )
    }
}