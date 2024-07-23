package com.haltec.silpusitron.feature.submission.form.di

import com.haltec.silpusitron.common.di.commonModule
import com.haltec.silpusitron.feature.submission.form.data.remote.SubmissionDocRemoteDatasource
import com.haltec.silpusitron.feature.submission.form.data.remote.SubmissionDocService
import com.haltec.silpusitron.feature.submission.form.data.repository.SubmissionDocRepository
import com.haltec.silpusitron.feature.submission.form.domain.ISubmissionDocRepository
import com.haltec.silpusitron.feature.submission.form.domain.usecase.GetTemplateUseCase
import com.haltec.silpusitron.feature.submission.form.domain.usecase.SubmitSubmissionUseCase
import com.haltec.silpusitron.feature.submission.form.ui.SubmissionDocViewModel
import com.haltec.silpusitron.shared.auth.di.authSharedModule
import com.haltec.silpusitron.shared.formprofile.di.formProfileModule
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