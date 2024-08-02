package com.silpusitron.feature.officertask.common.di

import com.silpusitron.feature.officertask.common.data.remote.DocApprovalRemoteDataSource
import com.silpusitron.feature.officertask.common.data.remote.DocApprovalService
import com.silpusitron.feature.officertask.common.data.repository.DocApprovalRepository
import com.silpusitron.feature.officertask.common.domain.IDocApprovalRepository
import com.silpusitron.feature.officertask.common.domain.RejectingUseCase
import com.silpusitron.feature.officertask.common.domain.SigningUseCase
import com.silpusitron.feature.officertask.docapproval.DocPreviewApprovalViewModel
import com.silpusitron.feature.officertask.tasks.data.OfficerTasksRemoteDataSource
import com.silpusitron.feature.officertask.tasks.data.OfficerTasksRepository
import com.silpusitron.feature.officertask.tasks.data.OfficerTasksService
import com.silpusitron.feature.officertask.tasks.domain.GetSubmittedLettersUseCase
import com.silpusitron.feature.officertask.tasks.domain.IOfficerTasksRepository
import com.silpusitron.feature.officertask.tasks.ui.OfficerTasksViewModel
import com.silpusitron.shared.lettertype.letterTypeModule
import com.silpusitron.shared.pdfviewer.di.pdfViewerModule
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val officerTaskModules = module {
    includes(letterTypeModule, pdfViewerModule)
    factory { OfficerTasksService(getProperty("BASE_URL"), getProperty("API_VERSION")) }
    factory { OfficerTasksRemoteDataSource(get()) }
    factory<IOfficerTasksRepository> { OfficerTasksRepository(get(), get(), get()) }
    factory { GetSubmittedLettersUseCase() }
    factory {
        com.silpusitron.feature.officertask.common.data.remote.DocApprovalService(
            getProperty(
                "BASE_URL"
            ), getProperty("API_VERSION")
        )
    }
    factory { com.silpusitron.feature.officertask.common.data.remote.DocApprovalRemoteDataSource(get()) }
    factory<IDocApprovalRepository> {
        com.silpusitron.feature.officertask.common.data.repository.DocApprovalRepository(
            get(),
            get(),
            get()
        )
    }
    factory { RejectingUseCase() }
    factory { SigningUseCase() }
    viewModel { OfficerTasksViewModel(get(), get(), get(), get()) }
    viewModel { DocPreviewApprovalViewModel(get(), get(), get()) }
}