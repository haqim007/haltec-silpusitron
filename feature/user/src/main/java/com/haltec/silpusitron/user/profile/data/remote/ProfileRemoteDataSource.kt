package com.haltec.silpusitron.user.profile.data.remote

import com.haltec.silpusitron.data.mechanism.getResult
import com.haltec.silpusitron.user.profile.data.remote.request.ProfileRequest

class ProfileRemoteDataSource(
    private val service: ProfileService
) {
    suspend fun getProfile(token: String) = getResult {
        service.getProfile(token)
    }

    suspend fun getOptions(token: String, path: FormOptionPath) = getResult {
        service.getFormOptions(token, path)
    }

    suspend fun getDistricts(token: String) = getResult {
        service.getDistricts(token)
    }

    suspend fun getSubDistricts(token: String, districtId: String?) = getResult {
        service.getSubDistricts(token, districtId)
    }

    suspend fun submitProfile(token: String, data: ProfileRequest) = getResult {
        service.submitProfile(token, data)
    }
}