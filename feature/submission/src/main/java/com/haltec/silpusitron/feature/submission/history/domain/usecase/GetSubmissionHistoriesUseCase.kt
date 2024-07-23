package com.haltec.silpusitron.feature.submission.history.domain.usecase

import androidx.paging.PagingData
import com.haltec.silpusitron.feature.submission.history.domain.ISubmissionHistoryRepository
import com.haltec.silpusitron.feature.submission.history.domain.SubmissionHistory
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDateTime
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetSubmissionHistoriesUseCase: KoinComponent {

    private val repository: ISubmissionHistoryRepository by inject()

    operator fun invoke(
        filterStartDate: LocalDateTime?,
        filterEndDate: LocalDateTime?,
        filterLetterStatus: String?,
        filterLetterTypeId: Int?,
        searchKeyword: String?,
    ): Flow<PagingData<SubmissionHistory>>{
        return repository.getHistories(
            filterStartDate,
            filterEndDate,
            filterLetterStatus,
            filterLetterTypeId,
            searchKeyword,
        )
    }
}
