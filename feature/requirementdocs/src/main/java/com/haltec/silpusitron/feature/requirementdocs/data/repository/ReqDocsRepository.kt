package com.haltec.silpusitron.feature.requirementdocs.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.haltec.silpusitron.common.util.DispatcherProvider
import com.haltec.silpusitron.feature.requirementdocs.data.pagingsource.ReqDocsPagingSource
import com.haltec.silpusitron.feature.requirementdocs.data.remote.ReqDocsRemoteDataSource
import com.haltec.silpusitron.feature.requirementdocs.domain.IReqDocsRepository
import com.haltec.silpusitron.feature.requirementdocs.domain.RequirementDoc
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

const val DEFAULT_ReqDocs_PAGES = 15
class ReqDocsRepository(
    private val remoteDataSource: ReqDocsRemoteDataSource,
    private val dispatcherProvider: DispatcherProvider
): IReqDocsRepository {
    override fun getData(): Flow<PagingData<RequirementDoc>> {
        return Pager(
            initialKey = 1,
            config = PagingConfig(
                pageSize = DEFAULT_ReqDocs_PAGES,
                enablePlaceholders = false,
                maxSize = 3*DEFAULT_ReqDocs_PAGES
            ),
            pagingSourceFactory = {
                ReqDocsPagingSource(remoteDataSource)
            }
        )
            .flow
            .flowOn(dispatcherProvider.io)
    }

}