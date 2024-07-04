package com.haltec.silpusitron.user.profile.data.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileInputOptionsResponse(
	@SerialName("data")
	val options: List<Option>
){
	@Serializable
	data class Option(
		val id: Int,
		val value: String,
		val key: String
	)
}


