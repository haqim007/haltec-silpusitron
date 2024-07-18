package com.haltec.silpusitron.user.account.di

import com.haltec.silpusitron.user.account.data.AccountRepository
import com.haltec.silpusitron.user.account.domain.IAccountRepository
import com.haltec.silpusitron.user.account.domain.LogoutUseCase
import com.haltec.silpusitron.user.account.ui.AccountViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val accountModule = module {
    factory<IAccountRepository> { AccountRepository(get(), get()) }
    factory { LogoutUseCase() }
    viewModel { AccountViewModel(get()) }
}