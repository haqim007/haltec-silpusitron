package com.haltec.silpusitron.feature.updateprofileofficer.data.repository

import com.haltec.silpusitron.common.util.DispatcherProvider
import com.haltec.silpusitron.data.mechanism.Resource
import com.haltec.silpusitron.shared.auth.mechanism.AuthorizedNetworkBoundResource
import com.haltec.silpusitron.shared.auth.preference.AuthPreference
import com.haltec.silpusitron.shared.form.domain.model.InputTextData
import com.haltec.silpusitron.shared.form.domain.model.TextValidationType
import com.haltec.silpusitron.feature.updateprofileofficer.data.remote.ProfileOfficerRemoteDataSource
import com.haltec.silpusitron.feature.updateprofileofficer.data.remote.ProfileOfficerResponse
import com.haltec.silpusitron.feature.updateprofileofficer.data.remote.SubmitProfileOfficerResponse
import com.haltec.silpusitron.feature.updateprofileofficer.data.toProfileData
import com.haltec.silpusitron.feature.updateprofileofficer.data.toProfileOfficerData
import com.haltec.silpusitron.feature.updateprofileofficer.data.toProfileRequest
import com.haltec.silpusitron.feature.updateprofileofficer.domain.FormProfileOfficerInputKey
import com.haltec.silpusitron.feature.updateprofileofficer.domain.IUpdateProfileOfficerRepository
import com.haltec.silpusitron.feature.updateprofileofficer.domain.ProfileOfficerData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromJsonElement

internal class UpdateProfileOfficerRepository(
    private val authPreference: AuthPreference,
    private val dispatcher: DispatcherProvider,
    private val remoteDataSource: ProfileOfficerRemoteDataSource
): IUpdateProfileOfficerRepository {
    override fun getProfile(): Flow<Resource<ProfileOfficerData>> {
        return object : AuthorizedNetworkBoundResource<ProfileOfficerData, ProfileOfficerResponse>(authPreference) {

            override suspend fun requestFromRemote(): Result<ProfileOfficerResponse> {
                return remoteDataSource.getProfile(getToken())
            }

            override fun loadResult(responseData: ProfileOfficerResponse): Flow<ProfileOfficerData> {
                return flowOf(responseData.toProfileOfficerData())
            }
        }
            .asFlow()
            .flowOn(dispatcher.io)
    }

    override fun submitProfile(
        data: ProfileOfficerData,
        input: Map<FormProfileOfficerInputKey, InputTextData<TextValidationType, String>>
    ): Flow<Resource<ProfileOfficerData>> {
        return object : AuthorizedNetworkBoundResource<ProfileOfficerData, SubmitProfileOfficerResponse>(
            authPreference
        ) {

            override suspend fun requestFromRemote(): Result<SubmitProfileOfficerResponse> {
                return remoteDataSource.submitProfile(getToken(), input.toProfileRequest())
            }

            override fun loadResult(responseData: SubmitProfileOfficerResponse): Flow<ProfileOfficerData> {
                return flowOf(responseData.toProfileData(data))
            }

            override fun loadResultOnError(responseData: JsonElement?): ProfileOfficerData? {
                if (responseData == null) return null

                return Json
                    .decodeFromJsonElement<SubmitProfileOfficerResponse>(responseData)
                    .toProfileData(data)
            }
        }
            .asFlow()
            .flowOn(dispatcher.io)
    }
}