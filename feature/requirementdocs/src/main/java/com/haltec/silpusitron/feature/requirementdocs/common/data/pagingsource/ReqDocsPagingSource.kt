package com.haltec.silpusitron.feature.requirementdocs.common.data.pagingsource

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.haltec.silpusitron.feature.requirementdocs.common.data.remote.ReqDocsRemoteDataSource
import com.haltec.silpusitron.feature.requirementdocs.common.data.repository.DEFAULT_ReqDocs_PAGES
import com.haltec.silpusitron.feature.requirementdocs.common.data.toModel
import com.haltec.silpusitron.feature.requirementdocs.common.domain.RequirementDoc
import io.ktor.serialization.JsonConvertException

const val ReqDocsPagingStartKey = 1

internal class ReqDocsPagingSource(
    private val remoteDataSource: ReqDocsRemoteDataSource,
    private val level: String?,
    private val letterTypeId: Int?,
    private val searchKeyword: String?
): PagingSource<Int, RequirementDoc>() {
    override fun getRefreshKey(state: PagingState<Int, RequirementDoc>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RequirementDoc> {
        val position = params.key ?: ReqDocsPagingStartKey

        return try {
            val docsResponse = remoteDataSource.getData(
                position, searchKeyword, level, letterTypeId
            )

            if (docsResponse.isSuccess){
                val docs = docsResponse.getOrNull()?.toModel() ?: listOf()
                val nextKey = if (docs.isEmpty() || docsResponse.getOrNull()?.data?.nextPageUrl == null) {
                    null
                } else {
                    // initial load size = 3 * NETWORK_PAGE_SIZE
                    // ensure we're not requesting duplicating items, at the 2nd request
                    position + (params.loadSize / DEFAULT_ReqDocs_PAGES)
                }
                LoadResult.Page(
                    data = docs,
                    prevKey = if (position == ReqDocsPagingStartKey) null else position - ReqDocsPagingStartKey,
                    nextKey = nextKey
                )
            } else {
                Log.e(
                    this::class.java.simpleName,
                    docsResponse.exceptionOrNull()?.message ?: "Unknown error on pagingsource"
                )
                LoadResult.Error(
                    docsResponse.exceptionOrNull()
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