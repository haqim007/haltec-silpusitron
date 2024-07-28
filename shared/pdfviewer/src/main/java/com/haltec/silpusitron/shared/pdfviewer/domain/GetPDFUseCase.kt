package com.haltec.silpusitron.shared.pdfviewer.domain

import com.haltec.silpusitron.data.mechanism.Resource
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.File

class GetPDFUseCase: KoinComponent {
    private val repository: IPDFViewerRepository by inject()

    operator fun invoke(title: String, fileUrl: String): Flow<Resource<File>> {
        return repository.getStreamedPDF(title, fileUrl)
    }
}