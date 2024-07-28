package com.haltec.silpusitron.feature.officertask.common.data.repository

import com.haltec.silpusitron.common.util.DispatcherProvider
import com.haltec.silpusitron.data.mechanism.Resource
import com.haltec.silpusitron.feature.officertask.common.data.remote.DocApprovalRemoteDataSource
import com.haltec.silpusitron.feature.officertask.common.data.remote.RejectingRequest
import com.haltec.silpusitron.feature.officertask.common.data.remote.SigningRequest
import com.haltec.silpusitron.feature.officertask.common.data.remote.SigningResponse
import com.haltec.silpusitron.feature.officertask.common.domain.IDocApprovalRepository
import com.haltec.silpusitron.shared.auth.mechanism.AuthorizedNetworkBoundResource
import com.haltec.silpusitron.shared.auth.preference.AuthPreference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn

class DocApprovalRepository(
    private val remoteDataSource: DocApprovalRemoteDataSource,
    private val authPreference: AuthPreference,
    private val dispatcher: DispatcherProvider
): IDocApprovalRepository {
    override fun signing(ids: List<Int>, passphrase: String): Flow<Resource<String>> {
        return object: AuthorizedNetworkBoundResource<String, SigningResponse>(authPreference){
            override suspend fun requestFromRemote(): Result<SigningResponse> {
                return remoteDataSource.signing(
                    getToken(),
                    SigningRequest(
                        ids = ids.joinToString(","),
                        passphrase = passphrase
                    )
                )
            }

            override fun loadResult(responseData: SigningResponse): Flow<String> {
                return flowOf(responseData.message)
            }

        }.asFlow()
            .flowOn(dispatcher.io)
    }

    override fun rejecting(ids: List<Int>, reason: String): Flow<Resource<String>> {
        return object: AuthorizedNetworkBoundResource<String, SigningResponse>(authPreference){
            override suspend fun requestFromRemote(): Result<SigningResponse> {
                return remoteDataSource.rejecting(
                    getToken(),
                    RejectingRequest(
                        ids = ids.joinToString(","),
                        reason = reason
                    )
                )
            }

            override fun loadResult(responseData: SigningResponse): Flow<String> {
                return flowOf(responseData.message)
            }

        }.asFlow()
            .flowOn(dispatcher.io)
    }
}