package com.haltec.silpusitron.feature.dashboard.data.repository

import com.haltec.silpusitron.common.util.DispatcherProvider
import com.haltec.silpusitron.data.mechanism.NetworkBoundResource
import com.haltec.silpusitron.data.mechanism.Resource
import com.haltec.silpusitron.data.preference.AuthPreference
import com.haltec.silpusitron.feature.dashboard.data.remote.DashboardRemoteDataSource
import com.haltec.silpusitron.feature.dashboard.data.remote.DashboardResponse
import com.haltec.silpusitron.feature.dashboard.data.toDashboardData
import com.haltec.silpusitron.feature.dashboard.domain.model.DashboardData
import com.haltec.silpusitron.feature.dashboard.domain.repository.IDashboardRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn

class DashboardRepository(
    private val dispatcher: DispatcherProvider,
    private val remoteDataSource: DashboardRemoteDataSource,
    private val preferences: AuthPreference
): IDashboardRepository {

    private suspend fun getToken(): String{
        return preferences.getToken().first()
    }

    override fun getData(): Flow<Resource<DashboardData>> {
        return object : NetworkBoundResource<DashboardData, DashboardResponse>() {
            override suspend fun requestFromRemote(): Result<DashboardResponse> {
                return remoteDataSource.getDashboard(getToken())
            }

            override fun loadResult(responseData: DashboardResponse): Flow<DashboardData> {
                return flowOf(responseData.toDashboardData())
            }

        }.asFlow()
            .flowOn(dispatcher.io)
    }
}