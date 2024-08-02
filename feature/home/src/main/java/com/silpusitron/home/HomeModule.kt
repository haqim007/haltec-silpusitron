package com.silpusitron.home

import com.silpusitron.feature.dashboard.user.di.dashboardUserModule
import com.silpusitron.feature.requirementdocs.common.di.requirementDocModule
import com.silpusitron.feature.settings.di.settingsModule
import com.silpusitron.feature.submission.common.di.submissionDocModule
import com.silpusitron.feature.submissionhistory.common.di.submissionHistoryModule
import com.silpusitron.feature.updateprofilecitizen.di.updateProfileCitizenModule
import org.koin.dsl.module

val homeModule = module {
    includes(
        dashboardUserModule,
        requirementDocModule,
        submissionDocModule,
        settingsModule,
        updateProfileCitizenModule,
        submissionHistoryModule
    )
}