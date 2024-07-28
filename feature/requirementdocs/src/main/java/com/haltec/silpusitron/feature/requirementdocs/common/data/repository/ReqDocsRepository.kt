package com.haltec.silpusitron.feature.requirementdocs.common.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.haltec.silpusitron.common.util.DispatcherProvider
import com.haltec.silpusitron.data.mechanism.NetworkBoundResource
import com.haltec.silpusitron.data.mechanism.Resource
import com.haltec.silpusitron.feature.requirementdocs.common.data.pagingsource.ReqDocsPagingSource
import com.haltec.silpusitron.feature.requirementdocs.common.data.remote.ReqDocsRemoteDataSource
import com.haltec.silpusitron.feature.requirementdocs.common.data.remote.response.LetterLevelsResponse
import com.haltec.silpusitron.feature.requirementdocs.common.domain.IReqDocsRepository
import com.haltec.silpusitron.feature.requirementdocs.common.domain.RequirementDoc
import com.haltec.silpusitron.shared.form.domain.model.InputOptions
import com.haltec.silpusitron.shared.form.domain.model.InputTextData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn

const val DEFAULT_ReqDocs_PAGES = 15
internal class ReqDocsRepository(
    private val remoteDataSource: ReqDocsRemoteDataSource,
    private val dispatcherProvider: DispatcherProvider
): IReqDocsRepository {
    override fun getData(
        searchKeyword: String?,
        letterTypeId: Int?,
        level: String?
    ): Flow<PagingData<RequirementDoc>> {
        return Pager(
            initialKey = 1,
            config = PagingConfig(
                pageSize = DEFAULT_ReqDocs_PAGES,
                enablePlaceholders = false,
                maxSize = 3* DEFAULT_ReqDocs_PAGES
            ),
            pagingSourceFactory = {
                ReqDocsPagingSource(
                    remoteDataSource,
                    searchKeyword = searchKeyword,
                    level = level, letterTypeId = letterTypeId)
            }
        )
            .flow
            .flowOn(dispatcherProvider.io)
    }

    override fun getLetterLevelOptions(): Flow<Resource<InputOptions>> {
        return object: NetworkBoundResource<InputOptions, LetterLevelsResponse>(){
            override suspend fun requestFromRemote(): Result<LetterLevelsResponse> {
                return remoteDataSource.getLetterLevels()
            }

            override fun loadResult(responseData: LetterLevelsResponse): Flow<InputOptions> {
                return flowOf(
                    InputOptions(
                        options = responseData.data.map {
                            InputTextData.Option(
                                value = it.level,
                                label = it.levelSurat,
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