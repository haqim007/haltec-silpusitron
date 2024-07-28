package com.haltec.silpusitron.feature.officertask.common.data.remote

import com.haltec.silpusitron.data.mechanism.getResult

class DocApprovalRemoteDataSource(
    private val service: DocApprovalService
){
    suspend fun signing(
        token: String,
        request: SigningRequest
    ) = getResult {
        service.signing(token, request)
    }

    suspend fun rejecting(
        token: String,
        request: RejectingRequest
    ) = getResult {
        service.rejecting(token, request)
    }
}