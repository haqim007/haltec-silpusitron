package com.haltec.silpusitron.feature.requirementdocs.data.remote

import com.haltec.silpusitron.data.mechanism.getResult

class ReqDocsRemoteDataSource(
    private val service: ReqDocsService
) {
    suspend fun getData(page: Int) = getResult {
        service.getData(page)
    }
}