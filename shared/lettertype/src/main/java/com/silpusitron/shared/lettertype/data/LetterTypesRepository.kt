package com.silpusitron.shared.lettertype.data

import com.silpusitron.common.util.DispatcherProvider
import com.silpusitron.data.mechanism.NetworkBoundResource
import com.silpusitron.data.mechanism.Resource
import com.silpusitron.shared.form.domain.model.InputOptions
import com.silpusitron.shared.form.domain.model.InputTextData
import com.silpusitron.shared.lettertype.domain.ILetterTypesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn

internal class LetterTypesRepository(
    private val remoteDataSource: LetterTypesRemoteDataSource,
    private val dispatcherProvider: DispatcherProvider
): ILetterTypesRepository {

    override fun getLetterTypeOptions(): Flow<Resource<InputOptions>> {
        return object: NetworkBoundResource<InputOptions, LetterTypesResponse>(){
            override suspend fun requestFromRemote(): Result<LetterTypesResponse> {
                return remoteDataSource.getLetterTypes()
            }

            override fun loadResult(responseData: LetterTypesResponse): Flow<InputOptions> {
                return flowOf(
                    InputOptions(
                        options = responseData.data.map {
                            InputTextData.Option(
                                value = it.jenisSuratId.toString(),
                                label = it.jenisSurat,
                                key = ""
                            )
                        }
                    )
                )
            }

        }.asFlow()
            .flowOn(dispatcherProvider.io)
    }

}