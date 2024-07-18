package com.haltec.silpusitron.user.profile.data.remote.response

import com.haltec.silpusitron.shared.formprofile.data.remote.response.ProfileFieldsErrorResponse
import kotlinx.serialization.Serializable

@Serializable
data class SubmitProfileResponse(
    val message: String,
    val errors: ProfileFieldsErrorResponse? = null,
)
