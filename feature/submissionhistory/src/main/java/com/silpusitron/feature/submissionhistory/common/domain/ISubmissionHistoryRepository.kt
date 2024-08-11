package com.silpusitron.feature.submissionhistory.common.domain

import androidx.paging.PagingData
import com.silpusitron.data.mechanism.Resource
import com.silpusitron.shared.form.domain.model.InputOptions
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDateTime
import java.io.File

internal interface ISubmissionHistoryRepository {
    fun getHistories(
        filterStartDate: LocalDateTime?,
        filterEndDate: LocalDateTime?,
        filterLetterStatus: String?,
        filterLetterTypeId: Int?,
        searchKeyword: String?
    ): Flow<PagingData<SubmissionHistory>>

    fun getLetterStatusOptions(): Flow<Resource<InputOptions>>
}
