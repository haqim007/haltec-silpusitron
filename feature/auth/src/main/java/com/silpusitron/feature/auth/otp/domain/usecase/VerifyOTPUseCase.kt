package com.silpusitron.feature.auth.otp.domain.usecase

import com.silpusitron.data.mechanism.Resource
import com.silpusitron.feature.auth.common.domain.IAuthRepository
import com.silpusitron.feature.auth.otp.domain.IOTPRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class VerifyOTPUseCase: KoinComponent {
    private val repository: IOTPRepository by inject()

    operator fun invoke(otp: String): Flow<Resource<Boolean>>{
        return repository.verifyOTP(otp)
    }
}