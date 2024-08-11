package com.silpusitron.feature.submissionhistory.common.di

//import com.silpusitron.feature.submissionhistory.common.domain.usecase.GetLetterTypeOptionsUseCase
import com.silpusitron.common.di.commonModule
import com.silpusitron.data.di.dataModule
import com.silpusitron.feature.submissionhistory.common.data.SubmissionHistoryRepository
import com.silpusitron.feature.submissionhistory.common.data.remote.SubmissionHistoryRemoteDataSource
import com.silpusitron.feature.submissionhistory.common.data.remote.SubmissionHistoryService
import com.silpusitron.feature.submissionhistory.common.domain.ISubmissionHistoryRepository
import com.silpusitron.feature.submissionhistory.common.domain.usecase.GetLetterStatusOptionsUseCase
import com.silpusitron.feature.submissionhistory.common.domain.usecase.GetSubmissionHistoriesUseCase
import com.silpusitron.feature.submissionhistory.docpreview.DocPreviewViewModel
import com.silpusitron.feature.submissionhistory.histories.SubmissionHistoriesViewModel
import com.silpusitron.shared.auth.di.authSharedModule
import com.silpusitron.shared.lettertype.domain.GetLetterTypeOptionsUseCase
import com.silpusitron.shared.pdfviewer.di.pdfViewerModule
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val submissionHistoryModule = module {
    includes(commonModule, dataModule, authSharedModule, pdfViewerModule)
    factory { SubmissionHistoryService(getProperty("BASE_URL"), getProperty("API_VERSION")) }
    factory { SubmissionHistoryRemoteDataSource(get()) }
    factory <ISubmissionHistoryRepository> { SubmissionHistoryRepository(get(), get(), get()) }
    factory { GetSubmissionHistoriesUseCase() }
    factory { GetLetterTypeOptionsUseCase() }
    factory { GetLetterStatusOptionsUseCase() }
    viewModel { SubmissionHistoriesViewModel(get(), get(), get()) }
    viewModel { DocPreviewViewModel(get()) }
}