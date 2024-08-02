package com.silpusitron.feature.submission.form.di

import com.silpusitron.common.di.commonModule
import com.silpusitron.feature.submission.form.data.remote.SubmissionDocRemoteDatasource
import com.silpusitron.feature.submission.form.data.remote.SubmissionDocService
import com.silpusitron.feature.submission.form.data.repository.SubmissionDocRepository
import com.silpusitron.feature.submission.form.domain.ISubmissionDocRepository
import com.silpusitron.feature.submission.form.domain.usecase.GetTemplateUseCase
import com.silpusitron.feature.submission.form.domain.usecase.SubmitSubmissionUseCase
import com.silpusitron.feature.submission.form.ui.SubmissionDocViewModel
import com.silpusitron.shared.auth.di.authSharedModule
import com.silpusitron.shared.formprofile.di.formProfileModule
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

internal val submissionFormModule = module {
    includes(commonModule, formProfileModule, authSharedModule)
    factory { SubmissionDocService(getProperty("BASE_URL"), getProperty("API_VERSION")) }
    factory { SubmissionDocRemoteDatasource(get()) }
    factory<ISubmissionDocRepository> { SubmissionDocRepository(get(), get(), get()) }
    factory { GetTemplateUseCase() }
    factory { SubmitSubmissionUseCase() }
    viewModel { SubmissionDocViewModel(get(), get()) }
}