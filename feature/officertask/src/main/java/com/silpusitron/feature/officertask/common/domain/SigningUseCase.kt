package com.silpusitron.feature.officertask.common.domain

import com.silpusitron.data.mechanism.Resource
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SigningUseCase: KoinComponent {
    private val repository: IDocApprovalRepository by inject()

    operator fun invoke(ids: List<Int>, passphrase: String): Flow<Resource<String>>{
        return repository.signing(ids, passphrase)
    }
}