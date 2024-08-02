package com.silpusitron.shared.pdfviewer.di

import com.silpusitron.common.di.commonModule
import com.silpusitron.data.di.dataModule
import com.silpusitron.shared.auth.di.authSharedModule
import com.silpusitron.shared.pdfviewer.data.PDFViewerService
import com.silpusitron.shared.pdfviewer.data.PDFViewerRemoteDataSource
import com.silpusitron.shared.pdfviewer.data.PDFViewerRepository
import com.silpusitron.shared.pdfviewer.domain.GetPDFUseCase
import com.silpusitron.shared.pdfviewer.domain.IPDFViewerRepository
import org.koin.dsl.module

val pdfViewerModule = module {
    includes(commonModule, dataModule, authSharedModule)
    factory { PDFViewerService(getProperty("BASE_URL"), getProperty("API_VERSION"), get()) }
    factory { PDFViewerRemoteDataSource(get()) }
    factory <IPDFViewerRepository> { PDFViewerRepository(get(), get(), get()) }
    factory { GetPDFUseCase() }
}