package com.silpusitron.shared.formprofile.data.remote.response

import com.silpusitron.shared.formprofile.data.remote.response.ProfileFieldsErrorResponse
import kotlinx.serialization.Serializable

@Serializable
internal data class SubmitProfileResponse(
    val message: String,
    val errors: ProfileFieldsErrorResponse? = null,
)
