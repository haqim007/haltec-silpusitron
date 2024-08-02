package com.silpusitron.feature.auth.common.domain

sealed class UserType(val value: String) {
    data object CITIZEN : UserType("0")
    data object OFFICER : UserType("1")
}