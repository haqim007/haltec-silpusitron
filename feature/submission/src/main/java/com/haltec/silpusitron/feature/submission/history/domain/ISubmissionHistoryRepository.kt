package com.haltec.silpusitron.feature.submission.history.domain

import androidx.paging.PagingData
import com.haltec.silpusitron.data.mechanism.Resource
import com.haltec.silpusitron.shared.form.domain.model.InputOptions
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDateTime

internal interface ISubmissionHistoryRepository {
    fun getHistories(
        filterStartDate: LocalDateTime?,
        filterEndDate: LocalDateTime?,
        filterLetterStatus: String?,
        filterLetterTypeId: Int?,
        searchKeyword: String?
    ): Flow<PagingData<SubmissionHistory>>

    fun getLetterTypeOptions(): Flow<Resource<InputOptions>>

}
