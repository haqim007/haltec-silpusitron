package com.haltec.silpusitron.feature.auth.otp.domain.usecase

import com.haltec.silpusitron.data.mechanism.Resource
import com.haltec.silpusitron.feature.auth.common.domain.IAuthRepository
import com.haltec.silpusitron.feature.auth.otp.domain.model.RequestOTPResult
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class RequestOTPUseCase : KoinComponent{
    private val repository: IAuthRepository by inject<IAuthRepository>()

    operator fun invoke(): Flow<Resource<RequestOTPResult>>{
        return repository.requestOTP()
    }
}