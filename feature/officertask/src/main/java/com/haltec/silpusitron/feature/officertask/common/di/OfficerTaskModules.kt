package com.haltec.silpusitron.feature.officertask.common.di

import com.haltec.silpusitron.feature.officertask.common.data.remote.DocApprovalRemoteDataSource
import com.haltec.silpusitron.feature.officertask.common.data.remote.DocApprovalService
import com.haltec.silpusitron.feature.officertask.common.data.repository.DocApprovalRepository
import com.haltec.silpusitron.feature.officertask.common.domain.IDocApprovalRepository
import com.haltec.silpusitron.feature.officertask.common.domain.RejectingUseCase
import com.haltec.silpusitron.feature.officertask.common.domain.SigningUseCase
import com.haltec.silpusitron.feature.officertask.docapproval.DocPreviewApprovalViewModel
import com.haltec.silpusitron.feature.officertask.tasks.data.OfficerTasksRemoteDataSource
import com.haltec.silpusitron.feature.officertask.tasks.data.OfficerTasksRepository
import com.haltec.silpusitron.feature.officertask.tasks.data.OfficerTasksService
import com.haltec.silpusitron.feature.officertask.tasks.domain.GetSubmittedLettersUseCase
import com.haltec.silpusitron.feature.officertask.tasks.domain.IOfficerTasksRepository
import com.haltec.silpusitron.feature.officertask.tasks.ui.OfficerTasksViewModel
import com.haltec.silpusitron.shared.lettertype.letterTypeModule
import com.haltec.silpusitron.shared.pdfviewer.di.pdfViewerModule
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val officerTaskModules = module {
    includes(letterTypeModule, pdfViewerModule)
    factory { OfficerTasksService(getProperty("BASE_URL"), getProperty("API_VERSION")) }
    factory { OfficerTasksRemoteDataSource(get()) }
    factory<IOfficerTasksRepository> { OfficerTasksRepository(get(), get(), get()) }
    factory { GetSubmittedLettersUseCase() }
    factory { DocApprovalService(getProperty("BASE_URL"), getProperty("API_VERSION")) }
    factory { DocApprovalRemoteDataSource(get()) }
    factory<IDocApprovalRepository> { DocApprovalRepository(get(), get(), get()) }
    factory { RejectingUseCase() }
    factory { SigningUseCase() }
    viewModel { OfficerTasksViewModel(get(), get(), get(), get()) }
    viewModel { DocPreviewApprovalViewModel(get(), get(), get()) }
}