package com.haltec.silpusitron.common.di

import com.haltec.silpusitron.common.util.AndroidDispatcher
import com.haltec.silpusitron.common.util.DispatcherProvider
import org.koin.dsl.module

val commonModule = module{
    single<DispatcherProvider>{ AndroidDispatcher() }
}