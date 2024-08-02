package com.silpusitron.app_petugas.di

import com.silpusitron.app_petugas.AppViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module { viewModel{ AppViewModel(get()) } }