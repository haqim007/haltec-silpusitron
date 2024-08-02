package com.silpusitron.common.di

import com.silpusitron.common.util.AndroidDispatcher
import com.silpusitron.common.util.DispatcherProvider
import org.koin.dsl.module

val commonModule = module{
    single<DispatcherProvider>{ AndroidDispatcher() }
}