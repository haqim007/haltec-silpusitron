package com.silpusitron.feature.submissionhistory.common.data.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LetterStatusesResponse(

	@SerialName("data")
	val data: List<LetterStatusResponse>
){
	@Serializable
	data class LetterStatusResponse(

		@SerialName("label")
		val label: String,

		@SerialName("value")
		val value: String
	)
}


