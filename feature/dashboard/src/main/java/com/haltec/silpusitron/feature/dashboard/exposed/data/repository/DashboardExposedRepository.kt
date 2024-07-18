package com.haltec.silpusitron.feature.dashboard.exposed.data.repository

import com.haltec.silpusitron.common.util.DispatcherProvider
import com.haltec.silpusitron.data.mechanism.NetworkBoundResource
import com.haltec.silpusitron.data.mechanism.Resource
import com.haltec.silpusitron.feature.dashboard.common.data.remote.response.DashboardResponse
import com.haltec.silpusitron.feature.dashboard.common.data.toDashboardData
import com.haltec.silpusitron.feature.dashboard.common.domain.model.DashboardData
import com.haltec.silpusitron.feature.dashboard.exposed.data.remote.DashboardExposedRemoteDataSource
import com.haltec.silpusitron.feature.dashboard.exposed.data.remote.NewsImagesResponse
import com.haltec.silpusitron.feature.dashboard.exposed.domain.model.NewsImage
import com.haltec.silpusitron.feature.dashboard.exposed.domain.repository.IDashboardExposedRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn

class DashboardExposedRepository(
    private val dispatcher: DispatcherProvider,
    private val remoteDataSource: DashboardExposedRemoteDataSource
): IDashboardExposedRepository {

    override fun getData(
        districtId: String?,
        startDate: String?,
        endDate: String?
    ): Flow<Resource<List<DashboardData>>> {
        return object : NetworkBoundResource<List<DashboardData>, DashboardResponse>() {
            override suspend fun requestFromRemote(): Result<DashboardResponse> {
                return remoteDataSource.getDashboard(
                    districtId, startDate, endDate
                )
            }

            override fun loadResult(responseData: DashboardResponse): Flow<List<DashboardData>> {
                return flowOf(responseData.toDashboardData())
            }

        }.asFlow()
            .flowOn(dispatcher.io)
    }

    override fun getNewsimages(): Flow<Resource<List<NewsImage>>>{
        return object : NetworkBoundResource<List<NewsImage>, NewsImagesResponse>() {
            override suspend fun requestFromRemote(): Result<NewsImagesResponse> {
                return remoteDataSource.getNewsImages()
            }

            override fun loadResult(responseData: NewsImagesResponse): Flow<List<NewsImage>> {
                return flowOf(responseData.data.map {
                    NewsImage(
                        title = it.title,
                        imageURL = it.image
                    )
                })
            }

        }.asFlow()
            .flowOn(dispatcher.io)
    }
}