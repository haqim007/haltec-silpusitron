package com.silpusitron.app

import android.app.Application
import com.silpusitron.common.di.commonModule
import com.silpusitron.data.di.dataModule
import com.silpusitron.app.di.appModule
import com.silpusitron.feature.auth.di.authModule
import com.silpusitron.feature.confirmprofilecitizen.di.confirmProfileCitizenModule
import com.silpusitron.feature.dashboard.exposed.di.dashboardExposedModule
import com.silpusitron.feature.requirementdocs.common.di.requirementDocModule
import com.silpusitron.home.homeModule
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
            properties(mapOf("IS_OFFICER" to BuildConfig.IS_OFFICER))
            properties(mapOf("RECAPTCHA_KEY_ID" to BuildConfig.RECAPTCHA_KEY_ID))
            // Load modules
             modules(
                 commonModule, dataModule, authModule, appModule,
                 dashboardExposedModule, requirementDocModule,
                 confirmProfileCitizenModule,
                 homeModule
             )
        }

    }
}