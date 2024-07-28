package com.haltec.silpusitron.feature.submissionhistory.common.data

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.haltec.silpusitron.data.mechanism.CustomThrowable
import com.haltec.silpusitron.feature.submissionhistory.common.data.remote.SubmissionHistoryRemoteDataSource
import com.haltec.silpusitron.feature.submissionhistory.common.domain.SubmissionHistory
import com.haltec.silpusitron.shared.auth.preference.AuthPreference
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.JsonConvertException
import kotlinx.coroutines.flow.first


const val SubmissionHistoriesPagingStartKey = 1
const val DEFAULT_SubmissionHistories_PAGES = 15


internal class SubmissionHistoriesPagingSource(
    private val remoteDataSource: SubmissionHistoryRemoteDataSource,
    private val authPreference: AuthPreference,
    private val startDate: String?,
    private val endDate: String?,
    private val letterType: String?,
    private val letterStatus: String?,
    private val searchKeyword: String?
): PagingSource<Int, SubmissionHistory>() {
    override fun getRefreshKey(state: PagingState<Int, SubmissionHistory>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SubmissionHistory> {
        val position = params.key ?: SubmissionHistoriesPagingStartKey

        return try {
            val historiesResponse = remoteDataSource.getHistories(
                token = authPreference.getToken().first(),
                page = position, 
                search = searchKeyword, 
                letterType = letterType,
                startDate = startDate,
                endDate = endDate,
                letterStatus = letterStatus
            )

            if (historiesResponse.isSuccess){
                val histories = historiesResponse.getOrNull()?.toModels() ?: listOf()
                val nextKey = if (histories.isEmpty() || historiesResponse.getOrNull()?.data?.nextPageUrl == null) {
                    null
                } else {
                    // initial load size = 3 * NETWORK_PAGE_SIZE
                    // ensure we're not requesting duplicating items, at the 2nd request
                    position + (params.loadSize / DEFAULT_SubmissionHistories_PAGES)
                }
                LoadResult.Page(
                    data = histories,
                    prevKey = if (position == SubmissionHistoriesPagingStartKey) null else position - SubmissionHistoriesPagingStartKey,
                    nextKey = nextKey
                )
            } else {

                val exception = historiesResponse.exceptionOrNull() as? CustomThrowable
                if (exception?.code == HttpStatusCode.Unauthorized){
                    authPreference.resetAuth()
                }

                Log.e(
                    this::class.java.simpleName,
                    historiesResponse.exceptionOrNull()?.message ?: "Unknown error on pagingsource"
                )
                LoadResult.Error(
                    historiesResponse.exceptionOrNull()
                        ?: Throwable(message = "Unknown error on pagingsource")
                )
            }

        }
        catch (exception: JsonConvertException){
            Log.e(
                this::class.java.simpleName,
                exception.message ?: "Unknown error on pagingsource"
            )
            return LoadResult.Error(exception)
        }
        catch (exception: Exception) {
            exception.message ?: "Unknown error on pagingsource"
            return LoadResult.Error(exception)
        }
    }


}