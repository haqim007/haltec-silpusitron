package com.haltec.silpusitron.home

import com.haltec.silpusitron.feature.dashboard.user.di.dashboardUserModule
import com.haltec.silpusitron.feature.requirementdocs.common.di.requirementDocModule
import com.haltec.silpusitron.feature.settings.di.settingsModule
import com.haltec.silpusitron.feature.submission.common.di.submissionDocModule
import com.haltec.silpusitron.feature.submissionhistory.common.di.submissionHistoryModule
import com.haltec.silpusitron.feature.updateprofilecitizen.di.updateProfileCitizenModule
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