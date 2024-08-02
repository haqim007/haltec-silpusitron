package com.silpusitron.feature.officertask.common.data.repository

import com.silpusitron.common.util.DispatcherProvider
import com.silpusitron.data.mechanism.Resource
import com.silpusitron.feature.officertask.common.data.remote.DocApprovalRemoteDataSource
import com.silpusitron.feature.officertask.common.data.remote.RejectingRequest
import com.silpusitron.feature.officertask.common.data.remote.SigningRequest
import com.silpusitron.feature.officertask.common.data.remote.SigningResponse
import com.silpusitron.feature.officertask.common.domain.IDocApprovalRepository
import com.silpusitron.shared.auth.mechanism.AuthorizedNetworkBoundResource
import com.silpusitron.shared.auth.preference.AuthPreference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn

class DocApprovalRepository(
    private val remoteDataSource: com.silpusitron.feature.officertask.common.data.remote.DocApprovalRemoteDataSource,
    private val authPreference: AuthPreference,
    private val dispatcher: DispatcherProvider
): IDocApprovalRepository {
    override fun signing(ids: List<Int>, passphrase: String): Flow<Resource<String>> {
        return object: AuthorizedNetworkBoundResource<String, SigningResponse>(authPreference){
            override suspend fun requestFromRemote(): Result<com.silpusitron.feature.officertask.common.data.remote.SigningResponse> {
                return remoteDataSource.signing(
                    getToken(),
                    com.silpusitron.feature.officertask.common.data.remote.SigningRequest(
                        ids = ids.joinToString(","),
                        passphrase = passphrase
                    )
                )
            }

            override fun loadResult(responseData: com.silpusitron.feature.officertask.common.data.remote.SigningResponse): Flow<String> {
                return flowOf(responseData.message)
            }

        }.asFlow()
            .flowOn(dispatcher.io)
    }

    override fun rejecting(ids: List<Int>, reason: String): Flow<Resource<String>> {
        return object: AuthorizedNetworkBoundResource<String, SigningResponse>(authPreference){
            override suspend fun requestFromRemote(): Result<com.silpusitron.feature.officertask.common.data.remote.SigningResponse> {
                return remoteDataSource.rejecting(
                    getToken(),
                    com.silpusitron.feature.officertask.common.data.remote.RejectingRequest(
                        ids = ids.joinToString(","),
                        reason = reason
                    )
                )
            }

            override fun loadResult(responseData: com.silpusitron.feature.officertask.common.data.remote.SigningResponse): Flow<String> {
                return flowOf(responseData.message)
            }

        }.asFlow()
            .flowOn(dispatcher.io)
    }
}