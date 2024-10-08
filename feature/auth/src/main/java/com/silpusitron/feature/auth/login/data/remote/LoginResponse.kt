package com.silpusitron.feature.auth.login.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val data: Data? = null,
    val message: String? = null,
    val errors: Errors? = null
){
	@Serializable
	data class Data(
		val username: String,
		val token: String,
		@SerialName("complete_data")
		val completeProfile: Boolean,
		@SerialName("no_hp")
		val phoneNumber: String? = null,
	)

	@Serializable
	data class Errors(
		val username: String? = null,
		val password: String? = null,
		@SerialName("g-recaptcha-response")
		val captcha: String? = null,
		@SerialName("tipe_user")
		val userType: String? = null
	)
}




