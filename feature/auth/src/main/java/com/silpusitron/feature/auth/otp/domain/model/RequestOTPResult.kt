package com.silpusitron.feature.auth.otp.domain.model


data class RequestOTPResult (
    val otpTimeout: Long,
    val phoneNumber: String
)