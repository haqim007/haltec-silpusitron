package com.haltec.silpusitron.shared.district.data.repository

import com.haltec.silpusitron.common.util.DispatcherProvider
import com.haltec.silpusitron.data.mechanism.NetworkBoundResource
import com.haltec.silpusitron.data.mechanism.Resource
import com.haltec.silpusitron.shared.district.data.remote.DistrictRemotesDataSource
import com.haltec.silpusitron.shared.district.data.remote.response.DistrictsResponse
import com.haltec.silpusitron.shared.district.domain.repository.IDistrictRepository
import com.haltec.silpusitron.shared.form.domain.model.InputOptions
import com.haltec.silpusitron.shared.form.domain.model.InputTextData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn


class DistrictRepository(
    private val remoteDataSource: DistrictRemotesDataSource,
    private val dispatcher: DispatcherProvider
): IDistrictRepository {

    override fun getDistricts(): Flow<Resource<InputOptions>> {
        return object : NetworkBoundResource<InputOptions, DistrictsResponse>() {

            override suspend fun requestFromRemote(): Result<DistrictsResponse> {
                return remoteDataSource.getDistricts()
            }

            override fun loadResult(responseData: DistrictsResponse): Flow<InputOptions> {
                return flowOf(
                    InputOptions(
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

}