package com.haltec.silpusitron.feature.auth.login.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class LoginResult(
    val username: String,
    val token: String
){
    @Serializable
    data class Error(
        val username: String? = null,
        val password: String? = null,
        @SerialName("g-recaptcha-response")
        val captcha: String? = null,
        @SerialName("tipe_user")
        val userType: String? = null
    )
}
