package com.haltec.silpusitron.feature.officertask.tasks.data

import com.haltec.silpusitron.data.remote.base.KtorService
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get

internal class OfficerTasksService(
    override val BASE_URL: String,
    override val API_VERSION: String
) : KtorService() {
    suspend fun getSubmittedLetters(
        token: String,
        page: Int,
        search: String?,
        startDate: String?,
        endDate: String?,
        letterType: String?,
    ): SubmittedLettersResponse {
        val params: MutableList<Pair<String, String>> = mutableListOf()
        params.add(Pair("page", page.toString()))
        if (!search.isNullOrEmpty()){
            params.add(Pair("search", search))
        }
        if (letterType != null){
            params.add(Pair("jenis_surat", letterType))
        }
        if (startDate != null){
            params.add(Pair("start_date", startDate))
        }
        if (endDate != null){
            params.add(Pair("end_date", endDate))
        }

        val response = client.get {
            endpoint("surat", params)
            bearerAuth(token)
        }

        checkOrThrowError(response)

        return response.body()
    }
}