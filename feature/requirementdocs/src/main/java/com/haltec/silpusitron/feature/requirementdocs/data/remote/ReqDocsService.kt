package com.haltec.silpusitron.feature.requirementdocs.data.remote

import com.haltec.silpusitron.data.remote.base.KtorService
import io.ktor.client.call.body
import io.ktor.client.request.get

class ReqDocsService (
    override val BASE_URL: String,
    override val API_VERSION: String
) : KtorService() {
    private val path = "template"

    suspend fun getData(page: Int): RequirementDocsResponse{
        val response = client.get {
            endpoint(path, parametersList = listOf(Pair("page", page.toString())))
        }

        checkOrThrowError(response)

        return response.body<RequirementDocsResponse>()
    }
}