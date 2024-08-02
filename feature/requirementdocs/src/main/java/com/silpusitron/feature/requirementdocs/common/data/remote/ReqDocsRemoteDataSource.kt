package com.silpusitron.feature.requirementdocs.common.data.remote

import com.silpusitron.data.mechanism.getResult
import com.silpusitron.feature.requirementdocs.common.data.remote.response.LetterLevelsResponse

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

    suspend fun getLetterLevels(): Result<LetterLevelsResponse> = getResult {
        service.getLetterLevels()
    }
}