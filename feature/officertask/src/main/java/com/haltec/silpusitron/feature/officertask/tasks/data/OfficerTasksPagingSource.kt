package com.haltec.silpusitron.feature.officertask.tasks.data

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.haltec.silpusitron.data.mechanism.CustomThrowable
import com.haltec.silpusitron.feature.officertask.tasks.domain.SubmittedLetter
import com.haltec.silpusitron.shared.auth.preference.AuthPreference
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.JsonConvertException
import kotlinx.coroutines.flow.first


internal const val DEFAULT_SUBMITTED_LETTER_PAGE_LIMIT = 15
internal const val SUBMITTED_LETTER_PAGE_START_KEY = 1

internal class OfficerTasksPagingSource(
    private val remoteDataSource: OfficerTasksRemoteDataSource,
    private val authPreference: AuthPreference,
    private val startDate: String?,
    private val endDate: String?,
    private val letterType: String?,
    private val searchKeyword: String?
): PagingSource<Int, SubmittedLetter>() {
    override fun getRefreshKey(state: PagingState<Int, SubmittedLetter>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SubmittedLetter> {
        val position = params.key ?: SUBMITTED_LETTER_PAGE_START_KEY

        return try {
            val response = remoteDataSource.getSubmittedLetters(
                token = authPreference.getToken().first(),
                page = position,
                search = searchKeyword,
                letterType = letterType,
                startDate = startDate,
                endDate = endDate,
            )

            if (response.isSuccess){
                val tasks = response.getOrNull()?.toModels() ?: listOf()
                val nextKey = if (tasks.isEmpty() || response.getOrNull()?.data?.nextPageUrl == null) {
                    null
                } else {
                    // initial load size = 3 * NETWORK_PAGE_SIZE
                    // ensure we're not requesting duplicating items, at the 2nd request
                    position + (params.loadSize / DEFAULT_SUBMITTED_LETTER_PAGE_LIMIT)
                }
                LoadResult.Page(
                    data = tasks,
                    prevKey = if (position == SUBMITTED_LETTER_PAGE_START_KEY) null
                    else position - SUBMITTED_LETTER_PAGE_START_KEY,
                    nextKey = nextKey
                )
            } else {

                val exception = response.exceptionOrNull() as? CustomThrowable
                if (exception?.code == HttpStatusCode.Unauthorized){
                    authPreference.resetAuth()
                }

                Log.e(
                    this::class.java.simpleName,
                    response.exceptionOrNull()?.message ?: "Unknown error on pagingsource"
                )
                LoadResult.Error(
                    response.exceptionOrNull()
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