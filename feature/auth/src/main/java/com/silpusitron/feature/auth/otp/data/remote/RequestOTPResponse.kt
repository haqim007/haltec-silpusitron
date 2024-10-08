package com.silpusitron.feature.auth.otp.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestOTPResponse(
	val data: Data? = null,
	val message: String? = null,
){
	@Serializable
	data class Data(
		@SerialName("otp_time")
		val otpTime: Long,
		val username: String,
		val token: String,
		@SerialName("no_hp")
		val phoneNumber: String?
	)
}



