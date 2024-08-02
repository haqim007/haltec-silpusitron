package com.silpusitron.shared.formprofile.data

import com.silpusitron.common.util.DispatcherProvider
import com.silpusitron.data.mechanism.Resource
import com.silpusitron.shared.auth.mechanism.AuthorizedNetworkBoundResource
import com.silpusitron.shared.auth.preference.AuthPreference
import com.silpusitron.shared.form.domain.model.InputOptions
import com.silpusitron.shared.form.domain.model.InputTextData
import com.silpusitron.shared.form.domain.model.TextValidationType
import com.silpusitron.shared.formprofile.data.remote.FormOptionPath
import com.silpusitron.shared.formprofile.data.remote.FormProfileRemoteDataSource
import com.silpusitron.shared.formprofile.data.remote.response.ProfileInputOptionsResponse
import com.silpusitron.shared.formprofile.data.remote.response.ProfileResponse
import com.silpusitron.shared.formprofile.data.remote.response.SubDistrictsResponse
import com.silpusitron.shared.formprofile.data.remote.response.SubmitProfileResponse
import com.silpusitron.shared.formprofile.domain.model.FormProfileInputKey
import com.silpusitron.shared.formprofile.domain.repository.IFormProfileRepository
import com.silpusitron.shared.formprofile.domain.model.ProfileData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromJsonElement

internal class FormProfileRepository(
    private val remoteDataSource: FormProfileRemoteDataSource,
    private val authPreference: AuthPreference,
    private val dispatcher: DispatcherProvider
): IFormProfileRepository {

    override fun getProfile(): Flow<Resource<ProfileData>> {
        return object : AuthorizedNetworkBoundResource<ProfileData, ProfileResponse>(authPreference) {

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

    override fun getOptions(optionPath: FormOptionPath): Flow<Resource<InputOptions>> {
        return object : AuthorizedNetworkBoundResource<InputOptions, ProfileInputOptionsResponse>(authPreference) {

            override suspend fun requestFromRemote(): Result<ProfileInputOptionsResponse> {
                return remoteDataSource.getOptions(getToken(), optionPath)
            }

            override fun loadResult(responseData: ProfileInputOptionsResponse): Flow<InputOptions> {
                return flowOf(
                    InputOptions(
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

    override fun getSubDistricts(districtId: String?): Flow<Resource<InputOptions>> {
        return object : AuthorizedNetworkBoundResource<InputOptions, SubDistrictsResponse>(authPreference) {

            override suspend fun requestFromRemote(): Result<SubDistrictsResponse> {
                return remoteDataSource.getSubDistricts(getToken(), districtId)
            }

            override fun loadResult(responseData: SubDistrictsResponse): Flow<InputOptions> {
                return flowOf(
                    InputOptions(
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