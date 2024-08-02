package com.silpusitron.feature.requirementdocs.common.di

import com.silpusitron.feature.requirementdocs.common.data.remote.ReqDocsRemoteDataSource
import com.silpusitron.feature.requirementdocs.common.data.remote.ReqDocsService
import com.silpusitron.feature.requirementdocs.common.data.repository.ReqDocsRepository
import com.silpusitron.feature.requirementdocs.common.domain.GetReqDocsUseCase
import com.silpusitron.feature.requirementdocs.common.domain.IReqDocsRepository
import com.silpusitron.feature.requirementdocs.simple.ui.SimpleReqDocViewModel
import com.silpusitron.feature.requirementdocs.submission.domain.usecase.GetLetterLevelOptionsUseCase
import com.silpusitron.feature.requirementdocs.submission.ui.ReqDocViewModel
import com.silpusitron.shared.lettertype.letterTypeModule
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val requirementDocModule = module {
    includes(letterTypeModule)
    factory { ReqDocsService(getProperty("BASE_URL"), getProperty("API_VERSION")) }
    factory { ReqDocsRemoteDataSource(get()) }
    factory<IReqDocsRepository> { ReqDocsRepository(get(), get()) }
    factory { GetReqDocsUseCase() }
    factory { GetLetterLevelOptionsUseCase() }
    viewModel { SimpleReqDocViewModel(get()) }
    viewModel { ReqDocViewModel(get(), get(), get()) }
}