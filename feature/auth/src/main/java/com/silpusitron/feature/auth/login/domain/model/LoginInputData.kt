package com.silpusitron.feature.auth.login.domain.model

import com.silpusitron.shared.form.domain.model.InputTextData
import com.silpusitron.shared.form.domain.model.TextValidationType
import com.silpusitron.feature.auth.common.domain.UserType

data class LoginInputData(
    val username: InputTextData<TextValidationType, String>,
    val password: InputTextData<TextValidationType, String>,
    val captcha: InputTextData<TextValidationType, String>,
    val userType: UserType
)
