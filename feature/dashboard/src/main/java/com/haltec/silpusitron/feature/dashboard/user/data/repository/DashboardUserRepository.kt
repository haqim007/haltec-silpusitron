package com.haltec.silpusitron.feature.dashboard.user.data.repository

import com.haltec.silpusitron.common.util.DispatcherProvider
import com.haltec.silpusitron.data.mechanism.Resource
import com.haltec.silpusitron.feature.dashboard.common.data.remote.response.DashboardResponse
import com.haltec.silpusitron.feature.dashboard.common.data.toDashboardData
import com.haltec.silpusitron.feature.dashboard.common.domain.model.DashboardData
import com.haltec.silpusitron.feature.dashboard.user.data.remote.DashboardUserRemoteDataSource
import com.haltec.silpusitron.feature.dashboard.user.domain.repository.IDashboardUserRepository
import com.haltec.silpusitron.shared.auth.mechanism.AuthorizedNetworkBoundResource
import com.haltec.silpusitron.shared.auth.preference.AuthPreference
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
}