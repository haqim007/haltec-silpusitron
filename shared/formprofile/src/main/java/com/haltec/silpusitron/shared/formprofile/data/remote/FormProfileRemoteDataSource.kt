package com.haltec.silpusitron.shared.formprofile.data.remote

import com.haltec.silpusitron.data.mechanism.getResult

internal class FormProfileRemoteDataSource(
    private val service: FormProfileService
) {
    suspend fun getProfile(token: String) = getResult {
        service.getProfile(token)
    }

    suspend fun getOptions(token: String, path: FormOptionPath) = getResult {
        service.getFormOptions(token, path)
    }

    suspend fun getSubDistricts(token: String, districtId: String?) = getResult {
        service.getSubDistricts(token, districtId)
    }

    suspend fun submitProfile(token: String, data: ProfileRequest) = getResult {
        service.submitProfile(token, data)
    }
}