package com.silpusitron.shared.pdfviewer.data

import com.silpusitron.data.mechanism.getResult
import java.io.File

class PDFViewerRemoteDataSource(
    private val service: PDFViewerService
) {
    suspend fun getStreamedPDF(
        token: String, title: String, fileUrl: String
    ): Result<File> = getResult {
        service.getStreamedPDF(token, title, fileUrl)
    }
}