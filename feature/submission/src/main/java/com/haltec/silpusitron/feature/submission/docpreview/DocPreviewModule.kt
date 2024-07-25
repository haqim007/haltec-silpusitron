package com.haltec.silpusitron.feature.submission.docpreview

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

internal val docPreviewModule = module {
    viewModel { DocPreviewViewModel(get()) }
}