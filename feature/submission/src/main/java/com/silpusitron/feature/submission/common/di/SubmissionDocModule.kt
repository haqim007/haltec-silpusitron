package com.silpusitron.feature.submission.common.di

import com.silpusitron.feature.submission.form.di.submissionFormModule
import org.koin.dsl.module

val submissionDocModule = module {
    includes(submissionFormModule)
}