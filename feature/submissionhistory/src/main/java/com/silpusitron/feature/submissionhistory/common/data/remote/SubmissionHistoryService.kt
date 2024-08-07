package com.silpusitron.feature.submissionhistory.common.data.remote

import com.silpusitron.common.util.DispatcherProvider
import com.silpusitron.data.remote.base.KtorService
import com.silpusitron.feature.submissionhistory.common.data.remote.response.LetterStatusesResponse
import com.silpusitron.feature.submissionhistory.common.data.remote.response.LetterTypesResponse
import com.silpusitron.feature.submissionhistory.common.data.remote.response.SubmissionHistoriesResponse
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.request.prepareGet
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.core.isNotEmpty
import io.ktor.utils.io.core.readBytes
import kotlinx.coroutines.withContext
import java.io.File

class SubmissionHistoryService(
    override val BASE_URL: String,
    override val API_VERSION: String,
    private val dispatcherProvider: DispatcherProvider
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

    suspend fun getLetterStatuses(token: String): LetterStatusesResponse {

        val response = client.get {
            endpoint("surat/list-status")
            bearerAuth(token)
        }

        checkOrThrowError(response)

        return response.body<LetterStatusesResponse>()
    }

}