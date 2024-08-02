package com.silpusitron.feature.updateprofileofficer.data.remote

import com.silpusitron.data.mechanism.getResult

internal class ProfileOfficerRemoteDataSource(
    private val service: ProfileOfficerService
) {
    suspend fun getProfile(token: String) = getResult {
        service.getProfile(token)
    }

    suspend fun submitProfile(token: String, data: ProfileOfficerRequest) = getResult {
        service.submitProfile(token, data)
    }
}