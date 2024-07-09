package com.haltec.silpusitron.feature.dashboard.exposed.data.repository

import com.haltec.silpusitron.common.util.DispatcherProvider
import com.haltec.silpusitron.data.mechanism.NetworkBoundResource
import com.haltec.silpusitron.data.mechanism.Resource
import com.haltec.silpusitron.data.preference.AuthPreference
import com.haltec.silpusitron.feature.dashboard.common.data.remote.response.DashboardResponse
import com.haltec.silpusitron.feature.dashboard.common.data.toDashboardData
import com.haltec.silpusitron.feature.dashboard.common.domain.model.DashboardData
import com.haltec.silpusitron.feature.dashboard.exposed.data.remote.DashboardExposedRemoteDataSource
import com.haltec.silpusitron.feature.dashboard.exposed.domain.repository.IDashboardExposedRepository
import com.haltec.silpusitron.feature.dashboard.user.data.remote.DashboardUserRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn

class DashboardExposedRepository(
    private val dispatcher: DispatcherProvider,
    private val remoteDataSource: DashboardExposedRemoteDataSource,
    private val preferences: AuthPreference
): IDashboardExposedRepository {

    override fun getData(): Flow<Resource<List<DashboardData>>> {
        return object : NetworkBoundResource<List<DashboardData>, DashboardResponse>() {
            override suspend fun requestFromRemote(): Result<DashboardResponse> {
                return remoteDataSource.getDashboard()
            }

            override fun loadResult(responseData: DashboardResponse): Flow<List<DashboardData>> {
                return flowOf(responseData.toDashboardData())
            }

        }.asFlow()
            .flowOn(dispatcher.io)
    }
}