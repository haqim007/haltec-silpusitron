package com.haltec.silpusitron.app_petugas

import android.app.Application
import com.haltec.silpusitron.app_petugas.di.appModule
import com.haltec.silpusitron.common.di.commonModule
import com.haltec.silpusitron.data.di.dataModule
import com.haltec.silpusitron.feature.auth.di.authModule
import com.haltec.silpusitron.feature.dashboard.exposed.di.dashboardExposedModule
import com.haltec.silpusitron.homeofficer.homeOfficerModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin


class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            properties(mapOf("BASE_URL" to BuildConfig.BASE_URL))
            properties(mapOf("API_VERSION" to BuildConfig.API_VERSION))
            properties(mapOf("IS_FOR_PETUGAS" to BuildConfig.IS_FOR_PETUGAS))
            // Load modules
            modules(
                commonModule, dataModule, authModule, appModule,
                dashboardExposedModule,
                homeOfficerModule
            )
        }

    }
}