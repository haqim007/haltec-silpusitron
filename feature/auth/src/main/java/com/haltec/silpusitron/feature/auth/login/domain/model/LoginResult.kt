package com.haltec.silpusitron.feature.auth.login.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class LoginResult(
    val inputData: LoginInputData,
    val isProfileCompleted: Boolean? = null
)
