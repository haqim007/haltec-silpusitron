package com.haltec.silpusitron.feature.dashboard.common.di

import com.haltec.silpusitron.feature.dashboard.exposed.di.dashboardExposedModule
import com.haltec.silpusitron.feature.dashboard.user.di.dashboardUserModule
import org.koin.dsl.module

val dashboardModule = module {
    includes(dashboardUserModule, dashboardExposedModule)
}