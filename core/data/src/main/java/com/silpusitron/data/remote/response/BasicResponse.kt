package com.silpusitron.data.remote.response

import kotlinx.serialization.Serializable

@Serializable
data class BasicResponse(
	val message: String? = null,

)
