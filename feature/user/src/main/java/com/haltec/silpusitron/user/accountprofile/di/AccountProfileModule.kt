package com.haltec.silpusitron.user.accountprofile.di

import com.haltec.silpusitron.shared.formprofile.di.formProfileModule
import org.koin.dsl.module

val accountProfileModule = module {
    includes(formProfileModule)
}