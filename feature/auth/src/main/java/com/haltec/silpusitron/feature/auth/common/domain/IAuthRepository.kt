package com.haltec.silpusitron.feature.auth.common.domain

import com.haltec.silpusitron.core.domain.model.TextValidationType
import com.haltec.silpusitron.data.mechanism.Resource
import com.haltec.silpusitron.core.domain.model.InputTextData
import com.haltec.silpusitron.feature.auth.login.domain.model.LoginInputData
import com.haltec.silpusitron.feature.auth.login.domain.model.LoginResult
import com.haltec.silpusitron.feature.auth.otp.domain.model.RequestOTPResult
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