package com.haltec.silpusitron.feature.confirmprofilecitizen.di

import com.haltec.silpusitron.shared.formprofile.di.formProfileModule
import org.koin.dsl.module


val confirmProfileCitizenModule = module {
    includes(formProfileModule)
}