package com.haltec.silpusitron.feature.submission.history.data.remote

import com.haltec.silpusitron.data.remote.base.KtorService
import com.haltec.silpusitron.feature.submission.history.data.remote.response.LetterTypesResponse
import com.haltec.silpusitron.feature.submission.history.data.remote.response.SubmissionHistoriesResponse
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get

class SubmissionHistoryService(
    override val BASE_URL: String,
    override val API_VERSION: String
) : KtorService() {
    suspend fun getHistories(
        token: String,
        page: Int,
        search: String?,
        startDate: String?,
        endDate: String?,
        letterType: String?,
        letterStatus: String?,
    ): SubmissionHistoriesResponse {
        val params: MutableList<Pair<String, String>> = mutableListOf()
        params.add(Pair("page", page.toString()))
        if (!search.isNullOrEmpty()){
            params.add(Pair("search", search))
        }
        if (letterStatus!=null){
            params.add(Pair("status", letterStatus))
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
            endpoint("surat/rekap", params)
            bearerAuth(token)
        }

        checkOrThrowError(response)

        return response.body<SubmissionHistoriesResponse>()
    }

    suspend fun getLetterTypes(): LetterTypesResponse {

        val response = client.get {
            endpoint("jenis-surat")
        }

        checkOrThrowError(response)

        return response.body<LetterTypesResponse>()
    }
}