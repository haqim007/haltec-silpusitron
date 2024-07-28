package com.haltec.silpusitron.feature.officertask.tasks.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.haltec.silpusitron.common.util.DispatcherProvider
import com.haltec.silpusitron.feature.officertask.tasks.domain.IOfficerTasksRepository
import com.haltec.silpusitron.feature.officertask.tasks.domain.SubmittedLetter
import com.haltec.silpusitron.shared.auth.preference.AuthPreference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.datetime.LocalDateTime

internal class OfficerTasksRepository(
    private val dispatcher: DispatcherProvider,
    private val remoteDataSource: OfficerTasksRemoteDataSource,
    private val authPreference: AuthPreference
): IOfficerTasksRepository {
    override fun getSubmittedLetters(
        filterStartDate: LocalDateTime?,
        filterEndDate: LocalDateTime?,
        filterLetterTypeId: Int?,
        searchKeyword: String?
    ): Flow<PagingData<SubmittedLetter>> {
        val startDateString = filterStartDate?.let {
            "${it.year}-" +
                    "${it.monthNumber.toString().padStart(2, '0')}-" +
                    it.dayOfMonth.toString().padStart(2, '0')
        }
        val endDateString = filterEndDate?.let {
            "${it.year}-" +
                    "${it.monthNumber.toString().padStart(2, '0')}-" +
                    it.dayOfMonth.toString().padStart(2, '0')
        }

        return Pager(
            initialKey = SUBMITTED_LETTER_PAGE_START_KEY,
            config = PagingConfig(
                pageSize = DEFAULT_SUBMITTED_LETTER_PAGE_LIMIT,
                enablePlaceholders = false,
                maxSize = 3* DEFAULT_SUBMITTED_LETTER_PAGE_LIMIT
            ),
            pagingSourceFactory = {
                OfficerTasksPagingSource(
                    remoteDataSource = remoteDataSource,
                    authPreference = authPreference,
                    searchKeyword = searchKeyword,
                    startDate = startDateString,
                    endDate = endDateString,
                    letterType = filterLetterTypeId?.toString()
                )
            }
        )
            .flow
            .flowOn(dispatcher.io)
    }
}