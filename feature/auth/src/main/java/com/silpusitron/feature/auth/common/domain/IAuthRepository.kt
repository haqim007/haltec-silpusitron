package com.silpusitron.feature.auth.common.domain

import com.silpusitron.shared.form.domain.model.TextValidationType
import com.silpusitron.data.mechanism.Resource
import com.silpusitron.shared.form.domain.model.InputTextData
import com.silpusitron.feature.auth.login.domain.model.LoginResult
import com.silpusitron.feature.auth.otp.domain.model.RequestOTPResult
import kotlinx.coroutines.flow.Flow

interface IAuthRepository {
    fun login(
        username: InputTextData<TextValidationType, String>,
        password: InputTextData<TextValidationType, String>,
        captcha: InputTextData<TextValidationType, String>,
        userType: UserType
    ): Flow<Resource<LoginResult>>

    fun checkSession(): Flow<Boolean>

    suspend fun logout()

    fun verifyOTP(otp: String): Flow<Resource<Boolean>>

    fun requestOTP(): Flow<Resource<RequestOTPResult>>
}