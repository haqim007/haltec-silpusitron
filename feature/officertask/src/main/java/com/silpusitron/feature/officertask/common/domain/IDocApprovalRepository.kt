package com.silpusitron.feature.officertask.common.domain

import com.silpusitron.data.mechanism.Resource
import kotlinx.coroutines.flow.Flow
import java.security.cert.CertPathValidatorException.Reason

interface IDocApprovalRepository {
    fun signing(
        ids: List<Int>,
        passphrase: String
    ): Flow<Resource<String>>

    fun rejecting(
        ids: List<Int>,
        reason: String
    ): Flow<Resource<String>>
}