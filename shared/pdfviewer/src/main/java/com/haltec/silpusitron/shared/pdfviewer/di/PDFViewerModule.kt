package com.haltec.silpusitron.shared.pdfviewer.di

import com.haltec.silpusitron.common.di.commonModule
import com.haltec.silpusitron.data.di.dataModule
import com.haltec.silpusitron.shared.auth.di.authSharedModule
import com.haltec.silpusitron.shared.pdfviewer.data.PDFViewerService
import com.haltec.silpusitron.shared.pdfviewer.data.PDFViewerRemoteDataSource
import com.haltec.silpusitron.shared.pdfviewer.data.PDFViewerRepository
import com.haltec.silpusitron.shared.pdfviewer.domain.GetPDFUseCase
import com.haltec.silpusitron.shared.pdfviewer.domain.IPDFViewerRepository
import org.koin.dsl.module

val pdfViewerModule = module {
    includes(commonModule, dataModule, authSharedModule)
    factory { PDFViewerService(getProperty("BASE_URL"), getProperty("API_VERSION"), get()) }
    factory { PDFViewerRemoteDataSource(get()) }
    factory <IPDFViewerRepository> { PDFViewerRepository(get(), get(), get()) }
    factory { GetPDFUseCase() }
}