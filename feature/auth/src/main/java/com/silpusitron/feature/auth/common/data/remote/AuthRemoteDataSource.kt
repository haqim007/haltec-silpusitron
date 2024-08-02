package com.silpusitron.feature.auth.common.data.remote

import com.silpusitron.data.mechanism.getResult
import com.silpusitron.feature.auth.login.data.remote.LoginRequest
import com.silpusitron.feature.auth.login.data.remote.LoginResponse
import com.silpusitron.feature.auth.otp.data.remote.RequestOTPResponse
import com.silpusitron.feature.auth.otp.data.remote.VerifyOTPRequest
import com.silpusitron.feature.auth.otp.data.remote.VerifyOTPResponse

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