package com.haltec.silpusitron.feature.auth.otp.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class VerifyOTPRequest(
    val otp: String
)
