package com.haltec.silpusitron.shared.lettertype.data

import com.haltec.silpusitron.data.mechanism.getResult

internal class LetterTypesRemoteDataSource(
    private val service: LetterTypesService
) {
    suspend fun getLetterTypes(): Result<LetterTypesResponse> = getResult {
        service.getLetterTypes()
    }
}