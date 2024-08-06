package com.silpusitron.feature.auth.phonenumberupdate.domain.repository

import com.silpusitron.data.mechanism.Resource
import kotlinx.coroutines.flow.Flow

interface IPhoneNumberRepository {
    fun update(phoneNumber: String): Flow<Resource<String>>
}
