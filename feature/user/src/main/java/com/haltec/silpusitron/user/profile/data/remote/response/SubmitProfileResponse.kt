package com.haltec.silpusitron.user.profile.data.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SubmitProfileResponse(
    val message: String,
    val errors: ProfileFieldsErrorResponse? = null,
)
