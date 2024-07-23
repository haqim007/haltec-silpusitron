package com.haltec.silpusitron.home

import com.haltec.silpusitron.feature.dashboard.common.di.dashboardModule
import com.haltec.silpusitron.feature.requirementdocs.common.di.requirementDocModule
import com.haltec.silpusitron.feature.submission.common.di.submissionDocModule
import com.haltec.silpusitron.user.account.di.accountModule
import com.haltec.silpusitron.user.accountprofile.di.accountProfileModule
import org.koin.dsl.module

val homeModule = module {
    includes(
        dashboardModule,
        requirementDocModule,
        submissionDocModule,
        accountModule,
        accountProfileModule
    )
}