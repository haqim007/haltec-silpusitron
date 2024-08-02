package com.silpusitron.feature.officertask.common.data.remote

import com.silpusitron.data.mechanism.getResult

class DocApprovalRemoteDataSource(
    private val service: com.silpusitron.feature.officertask.common.data.remote.DocApprovalService
){
    suspend fun signing(
        token: String,
        request: com.silpusitron.feature.officertask.common.data.remote.SigningRequest
    ) = getResult {
        service.signing(token, request)
    }

    suspend fun rejecting(
        token: String,
        request: com.silpusitron.feature.officertask.common.data.remote.RejectingRequest
    ) = getResult {
        service.rejecting(token, request)
    }
}