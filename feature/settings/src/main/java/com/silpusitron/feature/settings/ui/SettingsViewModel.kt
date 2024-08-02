package com.silpusitron.feature.settings.ui

import androidx.lifecycle.viewModelScope
import com.haltec.silpusitron.core.ui.ui.BaseViewModel
import com.silpusitron.feature.settings.domain.ISettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val repository: ISettingsRepository
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