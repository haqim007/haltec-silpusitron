package com.silpusitron.shared.pdfviewer.data

import com.silpusitron.common.util.DispatcherProvider
import com.silpusitron.data.remote.base.KtorService
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.prepareGet
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.core.isNotEmpty
import io.ktor.utils.io.core.readBytes
import kotlinx.coroutines.withContext
import java.io.File

class PDFViewerService(
    override val BASE_URL: String,
    override val API_VERSION: String,
    private val dispatcherProvider: DispatcherProvider
) : KtorService() {
    suspend fun getStreamedPDF(
        token: String,
        title: String,
        fileUrl: String
    ): File {
        val modifiedTitle = title
            .replace("/", "_")
            .replace("\\", "_")
            .replace(":", "")
            .replace("*", "")
            .replace("?", "")
            .replace("\"", "")
            .replace("<", "")
            .replace(">", "")
            .replace("|", "")

        return withContext(dispatcherProvider.io) {
            val file = File.createTempFile(modifiedTitle, ".pdf")
            client.prepareGet {
                url(fileUrl)
                bearerAuth(token)
                contentType(ContentType.Application.Json)
            }.execute { response ->
                if (response.contentType()?.toString() != "application/pdf") {
                    checkOrThrowError(response)
                }

                val channel = response.body<ByteReadChannel>()
                while (!channel.isClosedForRead){
                    val packet = channel.readRemaining(DEFAULT_BUFFER_SIZE.toLong())
                    while (packet.isNotEmpty){
                        val bytes = packet.readBytes()
                        file.appendBytes(bytes)
                    }
                }
            }

            return@withContext file
        }
    }
}