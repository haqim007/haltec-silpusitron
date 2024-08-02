package com.silpusitron.app.di

import com.silpusitron.app.AppViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module { viewModel{ AppViewModel(get()) } }