package com.haltec.silpusitron.feature.submission.common.di

import com.haltec.silpusitron.feature.submission.form.di.submissionFormModule
import com.haltec.silpusitron.feature.submission.history.di.submissionHistoryModule
import org.koin.dsl.module

val submissionDocModule = module {
    includes(submissionFormModule, submissionHistoryModule)
}