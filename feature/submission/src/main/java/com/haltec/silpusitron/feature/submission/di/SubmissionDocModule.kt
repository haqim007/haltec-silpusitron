package com.haltec.silpusitron.feature.submission.di

import com.haltec.silpusitron.common.di.commonModule
import com.haltec.silpusitron.feature.submission.data.remote.SubmissionDocRemoteDatasource
import com.haltec.silpusitron.feature.submission.data.remote.SubmissionDocService
import com.haltec.silpusitron.feature.submission.data.repository.SubmissionDocRepository
import com.haltec.silpusitron.feature.submission.domain.usecase.GetTemplateUseCase
import com.haltec.silpusitron.feature.submission.domain.ISubmissionDocRepository
import com.haltec.silpusitron.feature.submission.domain.usecase.SubmitSubmissionUseCase
import com.haltec.silpusitron.feature.submission.ui.SubmissionDocViewModel
import com.haltec.silpusitron.shared.auth.di.authSharedModule
import com.haltec.silpusitron.shared.formprofile.di.formProfileModule
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val submissionDocModule = module {
    includes(commonModule, formProfileModule, authSharedModule)
    factory { SubmissionDocService(getProperty("BASE_URL"), getProperty("API_VERSION")) }
    factory { SubmissionDocRemoteDatasource(get()) }
    factory<ISubmissionDocRepository> { SubmissionDocRepository(get(), get(), get()) }
    factory { GetTemplateUseCase() }
    factory { SubmitSubmissionUseCase() }
    viewModel { SubmissionDocViewModel(get(), get()) }
}