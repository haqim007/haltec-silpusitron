package com.haltec.silpusitron.feature.officertask.common.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RejectingRequest(
    @SerialName("id")
    val ids: String,
    @SerialName("keterangan")
    val reason: String
)
