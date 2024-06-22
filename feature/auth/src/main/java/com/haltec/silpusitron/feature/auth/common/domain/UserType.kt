package com.haltec.silpusitron.feature.auth.common.domain

sealed class UserType(val value: String) {
    data object APP : UserType("0")
    data object APP_PETUGAS : UserType("1")
}