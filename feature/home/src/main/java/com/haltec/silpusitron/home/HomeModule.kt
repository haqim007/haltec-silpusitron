package com.haltec.silpusitron.home

import com.haltec.silpusitron.feature.dashboard.common.di.dashboardModule
import org.koin.dsl.module

val homeModule = module {
    includes(dashboardModule)
}