package com.haltec.silpusitron.user.account.domain

interface IAccountRepository {
    suspend fun logout()
}