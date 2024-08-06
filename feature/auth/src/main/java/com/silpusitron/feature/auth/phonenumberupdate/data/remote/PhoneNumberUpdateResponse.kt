package com.silpusitron.feature.auth.phonenumberupdate.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PhoneNumberUpdateResponse(

	@SerialName("data")
	val data: Data? = null,

	@SerialName("message")
	val message: String? = null
){
	@Serializable
	data class Data(

		@SerialName("no_hp")
		val noHp: String? = null,

		@SerialName("otp_time")
		val otpTime: Int? = null,

		@SerialName("username")
		val username: String? = null,

		@SerialName("token")
		val token: String? = null,

		@SerialName("complete_data")
		val completeData: Boolean? = null
	)
}

