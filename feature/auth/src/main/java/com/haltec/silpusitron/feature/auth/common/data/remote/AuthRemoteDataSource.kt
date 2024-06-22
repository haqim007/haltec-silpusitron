package com.haltec.silpusitron.feature.auth.common.data.remote

import com.haltec.silpusitron.data.mechanism.getResult
import com.haltec.silpusitron.feature.auth.login.data.remote.LoginRequest
import com.haltec.silpusitron.feature.auth.login.data.remote.LoginResponse
import com.haltec.silpusitron.feature.auth.otp.data.remote.RequestOTPResponse
import com.haltec.silpusitron.feature.auth.otp.data.remote.VerifyOTPRequest
import com.haltec.silpusitron.feature.auth.otp.data.remote.VerifyOTPResponse

class AuthRemoteDataSource(
    private val service: AuthService
) {
    suspend fun login(request: LoginRequest): Result<LoginResponse> = getResult {
        service.login(request)
    }

    suspend fun verifyOTP(request: VerifyOTPRequest, token: String): Result<VerifyOTPResponse> = getResult {
        service.verifyOTP(request, token)
    }

    suspend fun requestOTP(token: String): Result<RequestOTPResponse> = getResult {
        service.requestOTP(token)
    }
}