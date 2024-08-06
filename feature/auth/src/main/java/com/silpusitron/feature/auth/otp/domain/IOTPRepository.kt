package com.silpusitron.feature.auth.otp.domain

import com.silpusitron.shared.form.domain.model.TextValidationType
import com.silpusitron.data.mechanism.Resource
import com.silpusitron.shared.form.domain.model.InputTextData
import com.silpusitron.feature.auth.login.domain.model.LoginResult
import com.silpusitron.feature.auth.otp.domain.model.RequestOTPResult
import kotlinx.coroutines.flow.Flow

interface IOTPRepository {

    fun verifyOTP(otp: String): Flow<Resource<Boolean>>

    fun requestOTP(): Flow<Resource<RequestOTPResult>>
}