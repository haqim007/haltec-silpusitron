package com.haltec.silpusitron.feature.updateprofilecitizen.di

import com.haltec.silpusitron.shared.formprofile.di.formProfileModule
import org.koin.dsl.module

val updateProfileCitizenModule = module {
    includes(formProfileModule)
}