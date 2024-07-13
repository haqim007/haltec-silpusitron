package com.haltec.silpusitron.feature.requirementdocs.common.di

import com.haltec.silpusitron.feature.requirementdocs.common.data.remote.ReqDocsRemoteDataSource
import com.haltec.silpusitron.feature.requirementdocs.common.data.remote.ReqDocsService
import com.haltec.silpusitron.feature.requirementdocs.common.data.repository.ReqDocsRepository
import com.haltec.silpusitron.feature.requirementdocs.common.domain.GetReqDocsUseCase
import com.haltec.silpusitron.feature.requirementdocs.common.domain.IReqDocsRepository
import com.haltec.silpusitron.feature.requirementdocs.simple.ui.SimpleReqDocViewModel
import com.haltec.silpusitron.feature.requirementdocs.submission.domain.usecase.GetLetterLevelOptionsUseCase
import com.haltec.silpusitron.feature.requirementdocs.submission.domain.usecase.GetLetterTypeOptionsUseCase
import com.haltec.silpusitron.feature.requirementdocs.submission.ui.ReqDocViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val requirementDocModule = module {
    factory { ReqDocsService(getProperty("BASE_URL"), getProperty("API_VERSION")) }
    factory { ReqDocsRemoteDataSource(get()) }
    factory<IReqDocsRepository> { ReqDocsRepository(get(), get()) }
    factory { GetReqDocsUseCase() }
    factory { GetLetterTypeOptionsUseCase() }
    factory { GetLetterLevelOptionsUseCase() }
    viewModel { SimpleReqDocViewModel(get()) }
    viewModel { ReqDocViewModel(get(), get(), get()) }
}