package com.haltec.silpusitron.homeofficer

import com.haltec.silpusitron.feature.dashboard.user.di.dashboardUserModule
import com.haltec.silpusitron.feature.officertask.common.di.officerTaskModules
import com.haltec.silpusitron.feature.settings.di.settingsModule
import com.haltec.silpusitron.feature.submission.common.di.submissionDocModule
import com.haltec.silpusitron.feature.submissionhistory.common.di.submissionHistoryModule
import com.haltec.silpusitron.feature.updateprofileofficer.di.updateProfileOfficerModule
import org.koin.dsl.module

val homeOfficerModule = module {
    includes(
        dashboardUserModule,
        submissionDocModule,
        settingsModule,
        updateProfileOfficerModule,
        officerTaskModules,
        submissionHistoryModule
    )
}