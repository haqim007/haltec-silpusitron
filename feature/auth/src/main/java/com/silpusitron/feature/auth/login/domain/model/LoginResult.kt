package com.silpusitron.feature.auth.login.domain.model

data class LoginResult(
    val inputData: LoginInputData,
    val isProfileCompleted: Boolean? = null
)
