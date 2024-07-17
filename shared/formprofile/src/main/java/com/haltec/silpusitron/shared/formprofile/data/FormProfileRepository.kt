package com.haltec.silpusitron.shared.formprofile.data

import com.haltec.silpusitron.common.util.DispatcherProvider
import com.haltec.silpusitron.data.mechanism.Resource
import com.haltec.silpusitron.shared.auth.mechanism.AuthorizedNetworkBoundResource
import com.haltec.silpusitron.shared.auth.preference.AuthPreference
import com.haltec.silpusitron.shared.form.domain.model.InputOptions
import com.haltec.silpusitron.shared.form.domain.model.InputTextData
import com.haltec.silpusitron.shared.formprofile.data.remote.FormOptionPath
import com.haltec.silpusitron.shared.formprofile.data.remote.FormProfileRemoteDataSource
import com.haltec.silpusitron.shared.formprofile.data.remote.response.ProfileInputOptionsResponse
import com.haltec.silpusitron.shared.formprofile.data.remote.response.ProfileResponse
import com.haltec.silpusitron.shared.formprofile.data.remote.response.SubDistrictsResponse
import com.haltec.silpusitron.shared.formprofile.domain.repository.IFormProfileRepository
import com.haltec.silpusitron.shared.formprofile.domain.model.ProfileData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn

internal class FormProfileRepository(
    private val remoteDataSource: FormProfileRemoteDataSource,
    private val authPreference: AuthPreference,
    private val dispatcher: DispatcherProvider
): IFormProfileRepository {

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

    override fun getOptions(optionPath: FormOptionPath): Flow<Resource<InputOptions>> {
        return object : AuthorizedNetworkBoundResource<InputOptions, ProfileInputOptionsResponse>(authPreference) {

            override suspend fun getToken(): String {
                return authPreference.getToken().first()
            }

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

            override suspend fun getToken(): String {
                return authPreference.getToken().first()
            }

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
}