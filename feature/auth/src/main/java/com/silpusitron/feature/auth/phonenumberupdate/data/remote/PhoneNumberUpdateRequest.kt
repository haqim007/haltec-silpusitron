package com.silpusitron.feature.auth.phonenumberupdate.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PhoneNumberUpdateRequest(
    @SerialName("no_hp")
    val phoneNumber: String
)
