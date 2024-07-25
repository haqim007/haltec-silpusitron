package com.haltec.silpusitron.user.profileold.data.repository

import com.haltec.silpusitron.common.util.DispatcherProvider
import com.haltec.silpusitron.data.mechanism.Resource
import com.haltec.silpusitron.shared.auth.mechanism.AuthorizedNetworkBoundResource
import com.haltec.silpusitron.shared.auth.preference.AuthPreference
import com.haltec.silpusitron.shared.form.domain.model.InputTextData
import com.haltec.silpusitron.shared.form.domain.model.TextValidationType
import com.haltec.silpusitron.shared.formprofile.domain.model.ProfileData
import com.haltec.silpusitron.user.profileold.data.remote.ProfileRemoteDataSource
import com.haltec.silpusitron.user.profileold.data.remote.response.SubmitProfileResponse
import com.haltec.silpusitron.user.profileold.data.toProfileData
import com.haltec.silpusitron.user.profileold.data.toProfileRequest
import com.haltec.silpusitron.user.profileold.domain.IProfileRepository
import com.haltec.silpusitron.shared.formprofile.domain.model.FormProfileInputKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromJsonElement

class ProfileRepository(
    private val remoteDataSource: ProfileRemoteDataSource,
    private val authPreference: AuthPreference,
    private val dispatcher: DispatcherProvider
): IProfileRepository {

    override fun submitProfile(
        data: ProfileData,
        input: Map<FormProfileInputKey, InputTextData<TextValidationType, String>>
    ): Flow<Resource<ProfileData>> {
        return object : AuthorizedNetworkBoundResource<ProfileData, SubmitProfileResponse>(
            authPreference
        ) {

            override suspend fun requestFromRemote(): Result<SubmitProfileResponse> {
                return remoteDataSource.submitProfile(getToken(), input.toProfileRequest())
            }

            override fun loadResult(responseData: SubmitProfileResponse): Flow<ProfileData> {
                return flowOf(responseData.toProfileData(data))
            }

            override fun loadResultOnError(responseData: JsonElement?): ProfileData? {
                if (responseData == null) return null

                return Json
                    .decodeFromJsonElement<SubmitProfileResponse>(responseData)
                    .toProfileData(data)
            }
        }
            .asFlow()
            .flowOn(dispatcher.io)
    }

}