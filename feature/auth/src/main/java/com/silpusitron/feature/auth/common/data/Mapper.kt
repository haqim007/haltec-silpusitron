package com.silpusitron.feature.auth.common.data

import com.silpusitron.shared.form.domain.model.TextValidationType
import com.silpusitron.feature.auth.common.domain.UserType
import com.silpusitron.feature.auth.login.data.remote.LoginResponse
import com.silpusitron.shared.form.domain.model.InputTextData
import com.silpusitron.feature.auth.login.domain.model.LoginInputData
import com.silpusitron.feature.auth.login.domain.model.LoginResult

fun LoginResponse.toModel(
    username: InputTextData<TextValidationType, String>,
    password: InputTextData<TextValidationType, String>,
    captcha: InputTextData<TextValidationType, String>,
    userType: UserType
) = LoginResult(
    inputData = LoginInputData(
        username = username.copy(
            message = this.errors?.username
        ),
        password = password.copy(
            message = this.errors?.password
        ),
        captcha = captcha.copy(
            message = this.errors?.captcha
        ),
        userType
    ),
    isProfileCompleted = this.data?.completeProfile
)
