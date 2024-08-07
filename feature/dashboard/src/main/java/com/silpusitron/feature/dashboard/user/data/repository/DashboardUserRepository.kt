package com.silpusitron.feature.dashboard.user.data.repository

import com.silpusitron.common.util.DispatcherProvider
import com.silpusitron.data.mechanism.NetworkBoundResource
import com.silpusitron.data.mechanism.Resource
import com.silpusitron.feature.dashboard.common.data.remote.response.DashboardResponse
import com.silpusitron.feature.dashboard.common.data.toDashboardData
import com.silpusitron.feature.dashboard.common.domain.model.DashboardData
import com.silpusitron.feature.dashboard.common.domain.model.NewsImage
import com.silpusitron.feature.dashboard.exposed.data.remote.NewsImagesResponse
import com.silpusitron.feature.dashboard.user.data.remote.DashboardUserRemoteDataSource
import com.silpusitron.feature.dashboard.user.domain.repository.IDashboardUserRepository
import com.silpusitron.shared.auth.mechanism.AuthorizedNetworkBoundResource
import com.silpusitron.shared.auth.preference.AuthPreference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn

internal class DashboardUserRepository(
    private val dispatcher: DispatcherProvider,
    private val remoteDataSource: DashboardUserRemoteDataSource,
    private val preferences: AuthPreference
): IDashboardUserRepository {

    override fun getData(): Flow<Resource<List<DashboardData>>> {
        return object : AuthorizedNetworkBoundResource<List<DashboardData>, DashboardResponse>(preferences) {

            override suspend fun requestFromRemote(): Result<DashboardResponse> {
                return remoteDataSource.getDashboard(getToken())
            }

            override fun loadResult(responseData: DashboardResponse): Flow<List<DashboardData>> {
                return flowOf(responseData.toDashboardData())
            }

        }.asFlow()
            .flowOn(dispatcher.io)
    }

    override fun getNewsImages(): Flow<Resource<List<NewsImage>>> {
        return object : AuthorizedNetworkBoundResource<List<NewsImage>, NewsImagesResponse>(preferences) {
            override suspend fun requestFromRemote(): Result<NewsImagesResponse> {
                return remoteDataSource.getNewsImages(getToken())
            }

            override fun loadResult(responseData: NewsImagesResponse): Flow<List<NewsImage>> {
                return flowOf(responseData.data.map {
                    NewsImage(
                        title = it.title,
                        imageURL = if (it.image.contains("https://")){
                            it.image
                        }else{
                            "https://${it.image.removePrefix("http://")}"
                        },
                        caption = it.description
                    )
                })
            }

        }.asFlow()
            .flowOn(dispatcher.io)
    }
}