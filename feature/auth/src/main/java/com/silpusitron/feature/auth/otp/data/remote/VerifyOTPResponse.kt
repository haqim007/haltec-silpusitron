package com.silpusitron.feature.auth.otp.data.remote

import com.silpusitron.feature.auth.login.data.remote.LoginResponse
import kotlinx.serialization.Serializable

@Serializable
data class VerifyOTPResponse(
	val data: Data? = null,
	val message: String? = null
){
	@Serializable
	data class Data(
		val username: String,
		val token: String
	)
}



