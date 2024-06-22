package com.haltec.silpusitron.feature.auth.login.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest (
    val username: String,
    val password: String,
    @SerialName("g-recaptcha-response")
    val captcha: String,
    @SerialName("tipe_user")
    val userType: String
)