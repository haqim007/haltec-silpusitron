package com.haltec.silpusitron.feature.submission.history.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.haltec.silpusitron.common.util.DispatcherProvider
import com.haltec.silpusitron.data.mechanism.NetworkBoundResource
import com.haltec.silpusitron.data.mechanism.Resource
import com.haltec.silpusitron.feature.submission.history.data.remote.SubmissionHistoryRemoteDataSource
import com.haltec.silpusitron.feature.submission.history.data.remote.response.LetterTypesResponse
import com.haltec.silpusitron.feature.submission.history.domain.ISubmissionHistoryRepository
import com.haltec.silpusitron.feature.submission.history.domain.SubmissionHistory
import com.haltec.silpusitron.shared.auth.preference.AuthPreference
import com.haltec.silpusitron.shared.form.domain.model.InputOptions
import com.haltec.silpusitron.shared.form.domain.model.InputTextData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.datetime.LocalDateTime

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

    override fun getLetterTypeOptions(): Flow<Resource<InputOptions>> {
        return object: NetworkBoundResource<InputOptions, LetterTypesResponse>(){
            override suspend fun requestFromRemote(): Result<LetterTypesResponse> {
                return remoteDataSource.getLetterTypes()
            }

            override fun loadResult(responseData: LetterTypesResponse): Flow<InputOptions> {
                return flowOf(
                    InputOptions(
                        options = responseData.data.map {
                            InputTextData.Option(
                                value = it.jenisSuratId.toString(),
                                label = it.jenisSurat,
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
