package com.haltec.silpusitron.shared.pdfviewer.domain

import com.haltec.silpusitron.data.mechanism.Resource
import kotlinx.coroutines.flow.Flow
import java.io.File

internal interface IPDFViewerRepository {
    fun getStreamedPDF(title: String, fileUrl: String): Flow<Resource<File>>
}
