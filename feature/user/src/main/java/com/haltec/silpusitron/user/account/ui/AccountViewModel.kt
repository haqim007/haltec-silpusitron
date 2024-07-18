package com.haltec.silpusitron.user.account.ui

import androidx.lifecycle.viewModelScope
import com.haltec.silpusitron.core.ui.ui.BaseViewModel
import com.haltec.silpusitron.user.account.domain.IAccountRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class AccountViewModel(
    private val repository: IAccountRepository
) : BaseViewModel<Nothing?, AccountUiAction>() {
    override val _state = MutableStateFlow(null)
    override fun doAction(action: AccountUiAction) {
        when(action){
            AccountUiAction.Logout -> viewModelScope.launch {
                repository.logout()
            }
        }
    }
}

sealed class AccountUiAction{
    data object Logout: AccountUiAction()
}