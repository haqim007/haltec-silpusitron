package com.silpusitron.feature.updateprofilecitizen.di

import com.silpusitron.shared.formprofile.di.formProfileModule
import org.koin.dsl.module

val updateProfileCitizenModule = module {
    includes(formProfileModule)
}