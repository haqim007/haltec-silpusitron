package com.silpusitron.feature.settings.data

import com.silpusitron.common.util.DispatcherProvider
import com.silpusitron.data.mechanism.Resource
import com.silpusitron.shared.auth.preference.AuthPreference
import com.silpusitron.feature.settings.domain.ISettingsRepository
import com.silpusitron.shared.auth.mechanism.AuthorizedNetworkBoundResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.withContext

class SettingsRepository(
    private val remoteDataSource: SettingsRemoteDataSource,
    private val authPreference: AuthPreference,
    private val dispatcherProvider: DispatcherProvider
): ISettingsRepository {
    override suspend fun logout(): Flow<Resource<String>> {
        return object : AuthorizedNetworkBoundResource<String, LogoutResponse>(authPreference){
            override suspend fun requestFromRemote(): Result<LogoutResponse> {
                return remoteDataSource.logout(getToken())
            }

            override fun loadResult(responseData: LogoutResponse): Flow<String> {
                return flowOf(responseData.message ?: "")
            }

            override suspend fun onComplete() {
                withContext(dispatcherProvider.io){
                    authPreference.resetAuth()
                }
            }

        }
            .asFlow()
    }
}