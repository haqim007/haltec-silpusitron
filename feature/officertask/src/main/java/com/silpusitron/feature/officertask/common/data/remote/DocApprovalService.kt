package com.silpusitron.feature.officertask.common.data.remote

import com.silpusitron.data.remote.base.KtorService
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class DocApprovalService(
    override val BASE_URL: String,
    override val API_VERSION: String
) : KtorService() {
    suspend fun signing(
        token: String,
        request: com.silpusitron.feature.officertask.common.data.remote.SigningRequest
    ): com.silpusitron.feature.officertask.common.data.remote.SigningResponse {
        val response = client.post {
            endpoint("surat/passphrase")
            setBody(request)
            bearerAuth(token)
        }

        checkOrThrowError(response)

        return response.body()
    }

    suspend fun rejecting(

        token: String,
        request: com.silpusitron.feature.officertask.common.data.remote.RejectingRequest
    ): com.silpusitron.feature.officertask.common.data.remote.SigningResponse {
        val response = client.post {
            endpoint("surat/reject")
            setBody(request)
            bearerAuth(token)
        }

        checkOrThrowError(response)

        return response.body()
    }
}