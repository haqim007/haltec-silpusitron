package com.silpusitron.feature.confirmprofilecitizen.di

import com.silpusitron.shared.formprofile.di.formProfileModule
import org.koin.dsl.module


val confirmProfileCitizenModule = module {
    includes(formProfileModule)
}