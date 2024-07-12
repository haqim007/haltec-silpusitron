package com.haltec.silpusitron.feature.requirementdocs.di

import com.haltec.silpusitron.feature.requirementdocs.data.remote.ReqDocsRemoteDataSource
import com.haltec.silpusitron.feature.requirementdocs.data.remote.ReqDocsService
import com.haltec.silpusitron.feature.requirementdocs.data.repository.ReqDocsRepository
import com.haltec.silpusitron.feature.requirementdocs.domain.GetReqDocsUseCase
import com.haltec.silpusitron.feature.requirementdocs.domain.IReqDocsRepository
import com.haltec.silpusitron.feature.requirementdocs.ui.ReqDocViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val requirementDocModule = module {
    factory { ReqDocsService(getProperty("BASE_URL"), getProperty("API_VERSION")) }
    factory { ReqDocsRemoteDataSource(get()) }
    factory<IReqDocsRepository> { ReqDocsRepository(get(), get()) }
    factory { GetReqDocsUseCase() }
    viewModel { ReqDocViewModel(get()) }
}