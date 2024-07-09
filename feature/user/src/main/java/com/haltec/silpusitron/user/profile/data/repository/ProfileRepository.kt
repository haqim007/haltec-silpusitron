package com.haltec.silpusitron.user.profile.data.repository

import com.haltec.silpusitron.common.util.DispatcherProvider
import com.haltec.silpusitron.core.domain.model.InputTextData
import com.haltec.silpusitron.core.domain.model.TextValidationType
import com.haltec.silpusitron.data.mechanism.AuthorizedNetworkBoundResource
import com.haltec.silpusitron.data.mechanism.Resource
import com.haltec.silpusitron.data.preference.AuthPreference
import com.haltec.silpusitron.user.profile.data.remote.FormOptionPath
import com.haltec.silpusitron.user.profile.data.remote.ProfileRemoteDataSource
import com.haltec.silpusitron.user.profile.data.remote.response.DistrictsResponse
import com.haltec.silpusitron.user.profile.data.remote.response.ProfileInputOptionsResponse
import com.haltec.silpusitron.user.profile.data.remote.response.ProfileResponse
import com.haltec.silpusitron.user.profile.data.remote.response.SubDistrictsResponse
import com.haltec.silpusitron.user.profile.data.remote.response.SubmitProfileResponse
import com.haltec.silpusitron.user.profile.data.toProfileData
import com.haltec.silpusitron.user.profile.data.toProfileRequest
import com.haltec.silpusitron.user.profile.domain.IProfileRepository
import com.haltec.silpusitron.user.profile.domain.model.FormProfileInputKey
import com.haltec.silpusitron.user.profile.domain.model.ProfileData
import com.haltec.silpusitron.user.profile.domain.model.ProfileInputOptions
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

    override fun getProfile(): Flow<Resource<ProfileData>> {
        return object : AuthorizedNetworkBoundResource<ProfileData, ProfileResponse>(authPreference) {

            override suspend fun getToken(): String {
                return authPreference.getToken().first()
            }

            override suspend fun requestFromRemote(): Result<ProfileResponse> {
                return remoteDataSource.getProfile(getToken())
            }

            override fun loadResult(responseData: ProfileResponse): Flow<ProfileData> {
                return flowOf(responseData.toProfileData())
            }
        }
            .asFlow()
            .flowOn(dispatcher.io)
    }

    override fun getOptions(optionPath: FormOptionPath): Flow<Resource<ProfileInputOptions>> {
        return object : AuthorizedNetworkBoundResource<ProfileInputOptions, ProfileInputOptionsResponse>(authPreference) {

            override suspend fun getToken(): String {
                return authPreference.getToken().first()
            }

            override suspend fun requestFromRemote(): Result<ProfileInputOptionsResponse> {
                return remoteDataSource.getOptions(getToken(), optionPath)
            }

            override fun loadResult(responseData: ProfileInputOptionsResponse): Flow<ProfileInputOptions> {
                return flowOf(
                    ProfileInputOptions(
                        options = responseData.options.map {
                            InputTextData.Option(
                                key = it.key,
                                label = it.value,
                                value = it.id.toString()
                            )
                        }
                    )
                )
            }

        }
            .asFlow()
            .flowOn(dispatcher.io)
    }

    override fun getDistricts(): Flow<Resource<ProfileInputOptions>> {
        return object : AuthorizedNetworkBoundResource<ProfileInputOptions, DistrictsResponse>(authPreference) {

            override suspend fun getToken(): String {
                return authPreference.getToken().first()
            }

            override suspend fun requestFromRemote(): Result<DistrictsResponse> {
                return remoteDataSource.getDistricts(getToken())
            }

            override fun loadResult(responseData: DistrictsResponse): Flow<ProfileInputOptions> {
                return flowOf(
                    ProfileInputOptions(
                        options = responseData.data.map {
                            InputTextData.Option(
                                value = it.id.toString(),
                                label = it.kecamatan,
                                key = "kecamatan_id"
                            )
                        }
                    )
                )
            }

        }
            .asFlow()
            .flowOn(dispatcher.io)
    }

    override fun getSubDistricts(districtId: String?): Flow<Resource<ProfileInputOptions>> {
        return object : AuthorizedNetworkBoundResource<ProfileInputOptions, SubDistrictsResponse>(authPreference) {

            override suspend fun getToken(): String {
                return authPreference.getToken().first()
            }

            override suspend fun requestFromRemote(): Result<SubDistrictsResponse> {
                return remoteDataSource.getSubDistricts(getToken(), districtId)
            }

            override fun loadResult(responseData: SubDistrictsResponse): Flow<ProfileInputOptions> {
                return flowOf(
                    ProfileInputOptions(
                        options = responseData.data.map {
                            InputTextData.Option(
                                value = it.id.toString(),
                                label = it.desa,
                                key = "desa_id"
                            )
                        }
                    )
                )
            }

        }
            .asFlow()
            .flowOn(dispatcher.io)
    }

    override fun submitProfile(
        data: ProfileData,
        input: Map<FormProfileInputKey, InputTextData<TextValidationType, String>>
    ): Flow<Resource<ProfileData>> {
        return object : AuthorizedNetworkBoundResource<ProfileData, SubmitProfileResponse>(
            authPreference
        ) {

            override suspend fun getToken(): String {
                return authPreference.getToken().first()
            }

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