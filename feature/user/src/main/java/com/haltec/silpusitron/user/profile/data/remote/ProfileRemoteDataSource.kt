package com.haltec.silpusitron.user.profile.data.remote

import com.haltec.silpusitron.data.mechanism.getResult
import com.haltec.silpusitron.user.profile.data.remote.request.ProfileRequest

class ProfileRemoteDataSource(
    private val service: ProfileService
) {

    suspend fun submitProfile(token: String, data: ProfileRequest) = getResult {
        service.submitProfile(token, data)
    }
}