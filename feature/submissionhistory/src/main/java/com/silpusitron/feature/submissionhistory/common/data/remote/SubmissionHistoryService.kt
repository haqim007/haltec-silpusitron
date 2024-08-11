package com.silpusitron.feature.submissionhistory.common.data.remote

import com.silpusitron.data.remote.base.KtorService
import com.silpusitron.feature.submissionhistory.common.data.remote.response.LetterStatusesResponse
import com.silpusitron.feature.submissionhistory.common.data.remote.response.SubmissionHistoriesResponse
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

    suspend fun getLetterStatuses(token: String): LetterStatusesResponse {

        val response = client.get {
            endpoint("surat/list-status")
            bearerAuth(token)
        }

        checkOrThrowError(response)

        return response.body<LetterStatusesResponse>()
    }

}