package com.silpusitron.feature.requirementdocs.common.data.remote

import com.silpusitron.data.remote.base.KtorService
import com.silpusitron.feature.requirementdocs.common.data.remote.response.LetterLevelsResponse
import com.silpusitron.feature.requirementdocs.common.data.remote.response.RequirementDocsResponse
import io.ktor.client.call.body
import io.ktor.client.request.get

internal class ReqDocsService (
    override val BASE_URL: String,
    override val API_VERSION: String
) : KtorService() {

    suspend fun getData(
        page: Int,
        search: String?,
        level: String?,
        letterTypeId: Int?
    ): RequirementDocsResponse {
        val params: MutableList<Pair<String, String>> = mutableListOf()
        params.add(Pair("page", page.toString()))
        if (search != null){
            params.add(Pair("search", search))
        }
        if (level!=null){
            params.add(Pair("level_surat", level))
        }
        if (letterTypeId != null){
            params.add(Pair("jenis_surat", letterTypeId.toString()))
        }
        val response = client.get {
            endpoint("template", parametersList = params.toList())
        }

        checkOrThrowError(response)

        return response.body<RequirementDocsResponse>()
    }

    suspend fun getLetterLevels(): LetterLevelsResponse {

        val response = client.get {
            endpoint("level-surat")
        }

        checkOrThrowError(response)

        return response.body<LetterLevelsResponse>()
    }
}