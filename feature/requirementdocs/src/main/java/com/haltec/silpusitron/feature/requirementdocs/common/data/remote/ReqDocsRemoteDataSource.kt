package com.haltec.silpusitron.feature.requirementdocs.common.data.remote

import com.haltec.silpusitron.data.mechanism.getResult
import com.haltec.silpusitron.feature.requirementdocs.common.data.remote.response.LetterLevelsResponse
import com.haltec.silpusitron.feature.requirementdocs.common.data.remote.response.LetterTypesResponse

internal class ReqDocsRemoteDataSource(
    private val service: ReqDocsService
) {
    suspend fun getData(
        page: Int,
        searchKeyword: String?,
        level: String?,
        letterTypeId: Int?
    ) = getResult {
        service.getData(page, searchKeyword, level, letterTypeId)
    }

    suspend fun getLetterTypes(): Result<LetterTypesResponse> = getResult {
        service.getLetterTypes()
    }

    suspend fun getLetterLevels(): Result<LetterLevelsResponse> = getResult {
        service.getLetterLevels()
    }
}