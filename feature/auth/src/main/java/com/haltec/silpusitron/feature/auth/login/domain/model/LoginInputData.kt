package com.haltec.silpusitron.feature.auth.login.domain.model

import com.haltec.silpusitron.core.domain.model.InputTextData
import com.haltec.silpusitron.core.domain.model.TextValidationType
import com.haltec.silpusitron.feature.auth.common.domain.UserType

data class LoginInputData(
    val username: InputTextData<TextValidationType, String>,
    val password: InputTextData<TextValidationType, String>,
    val captcha: InputTextData<TextValidationType, String>,
    val userType: UserType
)
