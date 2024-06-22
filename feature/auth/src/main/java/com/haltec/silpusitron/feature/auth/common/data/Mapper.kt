package com.haltec.silpusitron.feature.auth.common.data

import com.haltec.silpusitron.core.domain.model.TextValidationType
import com.haltec.silpusitron.feature.auth.common.domain.UserType
import com.haltec.silpusitron.feature.auth.login.data.remote.LoginResponse
import com.haltec.silpusitron.core.domain.model.InputTextData
import com.haltec.silpusitron.feature.auth.login.domain.model.LoginInputData

fun LoginResponse.toModel(
    username: InputTextData<TextValidationType, String>,
    password: InputTextData<TextValidationType, String>,
    captcha: InputTextData<TextValidationType, String>,
    userType: UserType
) = LoginInputData(
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
)