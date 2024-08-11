package com.silpusitron.feature.submissionhistory.common.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.silpusitron.common.util.DispatcherProvider
import com.silpusitron.data.mechanism.NetworkBoundResource
import com.silpusitron.data.mechanism.Resource
import com.silpusitron.feature.submissionhistory.common.data.remote.SubmissionHistoryRemoteDataSource
import com.silpusitron.feature.submissionhistory.common.data.remote.response.LetterStatusesResponse
import com.silpusitron.feature.submissionhistory.common.data.remote.response.LetterTypesResponse
import com.silpusitron.feature.submissionhistory.common.domain.ISubmissionHistoryRepository
import com.silpusitron.feature.submissionhistory.common.domain.SubmissionHistory
import com.silpusitron.shared.auth.mechanism.AuthorizedNetworkBoundResource
import com.silpusitron.shared.auth.preference.AuthPreference
import com.silpusitron.shared.form.domain.model.InputOptions
import com.silpusitron.shared.form.domain.model.InputTextData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.datetime.LocalDateTime
import java.io.File

internal class SubmissionHistoryRepository(
    private val remoteDataSource: SubmissionHistoryRemoteDataSource,
    private val authPreference: AuthPreference,
    private val dispatcherProvider: DispatcherProvider
): ISubmissionHistoryRepository {

    override fun getHistories(
        filterStartDate: LocalDateTime?,
        filterEndDate: LocalDateTime?,
        filterLetterStatus: String?,
        filterLetterTypeId: Int?,
        searchKeyword: String?
    ): Flow<PagingData<SubmissionHistory>> {
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
            initialKey = 1,
            config = PagingConfig(
                pageSize = DEFAULT_SubmissionHistories_PAGES,
                enablePlaceholders = false,
                maxSize = 3* DEFAULT_SubmissionHistories_PAGES
            ),
            pagingSourceFactory = {
                SubmissionHistoriesPagingSource(
                    remoteDataSource = remoteDataSource,
                    authPreference = authPreference,
                    searchKeyword = searchKeyword,
                    startDate = startDateString,
                    endDate = endDateString,
                    letterStatus = filterLetterStatus,
                    letterType = filterLetterTypeId?.toString()
                )
            }
        )
            .flow
            .flowOn(dispatcherProvider.io)

    }

    override fun getLetterStatusOptions(): Flow<Resource<InputOptions>> {
        return object: AuthorizedNetworkBoundResource<InputOptions, LetterStatusesResponse>(authPreference){

            override suspend fun requestFromRemote(): Result<LetterStatusesResponse> {
                return remoteDataSource.getLetterStatuses(
                    getToken()
                )
            }

            override fun loadResult(responseData: LetterStatusesResponse): Flow<InputOptions> {
                return flowOf(
                    InputOptions(
                        options = responseData.data.map {
                            InputTextData.Option(
                                value = it.value,
                                label = it.label,
                                key = ""
                            )
                        }
                    )
                )
            }

        }.asFlow()
            .flowOn(dispatcherProvider.io)
    }
}
