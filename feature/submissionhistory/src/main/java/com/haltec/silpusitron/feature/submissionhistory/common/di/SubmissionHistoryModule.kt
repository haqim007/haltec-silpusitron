package com.haltec.silpusitron.feature.submissionhistory.common.di

import com.haltec.silpusitron.common.di.commonModule
import com.haltec.silpusitron.data.di.dataModule
import com.haltec.silpusitron.feature.submissionhistory.docpreview.DocPreviewViewModel
import com.haltec.silpusitron.feature.submissionhistory.common.data.SubmissionHistoryRepository
import com.haltec.silpusitron.feature.submissionhistory.common.data.remote.SubmissionHistoryRemoteDataSource
import com.haltec.silpusitron.feature.submissionhistory.common.data.remote.SubmissionHistoryService
import com.haltec.silpusitron.feature.submissionhistory.common.domain.usecase.GetSubmissionHistoriesUseCase
import com.haltec.silpusitron.feature.submissionhistory.common.domain.ISubmissionHistoryRepository
import com.haltec.silpusitron.feature.submissionhistory.common.domain.usecase.GetLetterStatusOptionsUseCase
import com.haltec.silpusitron.feature.submissionhistory.common.domain.usecase.GetLetterTypeOptionsUseCase
import com.haltec.silpusitron.feature.submissionhistory.histories.SubmissionHistoriesViewModel
import com.haltec.silpusitron.shared.auth.di.authSharedModule
import com.haltec.silpusitron.shared.pdfviewer.di.pdfViewerModule
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val submissionHistoryModule = module {
    includes(commonModule, dataModule, authSharedModule, pdfViewerModule)
    factory { SubmissionHistoryService(getProperty("BASE_URL"), getProperty("API_VERSION"), get()) }
    factory { SubmissionHistoryRemoteDataSource(get()) }
    factory <ISubmissionHistoryRepository> { SubmissionHistoryRepository(get(), get(), get()) }
    factory { GetSubmissionHistoriesUseCase() }
    factory { GetLetterTypeOptionsUseCase() }
    factory { GetLetterStatusOptionsUseCase() }
    viewModel { SubmissionHistoriesViewModel(get(), get(), get()) }
    viewModel { DocPreviewViewModel(get()) }
}