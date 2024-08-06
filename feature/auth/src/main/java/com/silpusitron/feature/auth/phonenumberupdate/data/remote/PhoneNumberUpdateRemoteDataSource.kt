package com.silpusitron.feature.auth.phonenumberupdate.data.remote

import com.silpusitron.data.mechanism.getResult

class PhoneNumberUpdateRemoteDataSource(
    private val service: PhoneNumberUpdateService
) {

    suspend fun update(token: String, phoneNumber: PhoneNumberUpdateRequest) = getResult {
        service.update(token, phoneNumber)
    }

}
