package com.haltec.silpusitron.home

import com.haltec.silpusitron.feature.dashboard.user.di.dashboardUserModule
import com.haltec.silpusitron.feature.requirementdocs.common.di.requirementDocModule
import com.haltec.silpusitron.feature.submission.common.di.submissionDocModule
import com.haltec.silpusitron.user.account.di.accountModule
import com.haltec.silpusitron.user.accountprofile.di.accountProfileModule
import org.koin.dsl.module

val homeModule = module {
    includes(
        dashboardUserModule,
        requirementDocModule,
        submissionDocModule,
        accountModule,
        accountProfileModule
    )
}