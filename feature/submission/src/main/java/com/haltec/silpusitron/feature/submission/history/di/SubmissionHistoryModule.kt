package com.haltec.silpusitron.feature.submission.history.di

import com.haltec.silpusitron.feature.submission.history.data.SubmissionHistoryRepository
import com.haltec.silpusitron.feature.submission.history.data.remote.SubmissionHistoryRemoteDataSource
import com.haltec.silpusitron.feature.submission.history.data.remote.SubmissionHistoryService
import com.haltec.silpusitron.feature.submission.history.domain.usecase.GetSubmissionHistoriesUseCase
import com.haltec.silpusitron.feature.submission.history.domain.ISubmissionHistoryRepository
import com.haltec.silpusitron.feature.submission.history.domain.usecase.GetLetterTypeOptionsUseCase
import com.haltec.silpusitron.feature.submission.history.ui.SubmissionHistoriesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

internal val submissionHistoryModule = module {
    factory { SubmissionHistoryService(getProperty("BASE_URL"), getProperty("API_VERSION")) }
    factory { SubmissionHistoryRemoteDataSource(get()) }
    factory <ISubmissionHistoryRepository> { SubmissionHistoryRepository(get(), get(), get()) }
    factory { GetSubmissionHistoriesUseCase() }
    factory { GetLetterTypeOptionsUseCase() }
    viewModel { SubmissionHistoriesViewModel(get(), get()) }
}