package com.silpusitron.homeofficer

import com.silpusitron.feature.dashboard.user.di.dashboardUserModule
import com.silpusitron.feature.officertask.common.di.officerTaskModules
import com.silpusitron.feature.requirementdocs.common.di.requirementDocModule
import com.silpusitron.feature.settings.di.settingsModule
import com.silpusitron.feature.submission.common.di.submissionDocModule
import com.silpusitron.feature.submissionhistory.common.di.submissionHistoryModule
import com.silpusitron.feature.updateprofileofficer.di.updateProfileOfficerModule
import org.koin.dsl.module

val homeOfficerModule = module {
    includes(
        dashboardUserModule,
        submissionDocModule,
        settingsModule,
        updateProfileOfficerModule,
        officerTaskModules,
        submissionHistoryModule,
        requirementDocModule,
    )
}