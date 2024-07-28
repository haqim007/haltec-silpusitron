package com.haltec.silpusitron.feature.officertask.common.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SigningRequest(
    @SerialName("id")
    val ids: String,
    @SerialName("passphrase")
    val passphrase: String
)
