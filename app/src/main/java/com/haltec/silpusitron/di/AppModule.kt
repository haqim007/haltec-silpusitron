package com.haltec.silpusitron.di

import com.haltec.silpusitron.AppViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module { viewModel{ AppViewModel(get()) } }