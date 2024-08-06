package com.silpusitron.feature.settings.ui

import androidx.lifecycle.viewModelScope
import com.silpusitron.core.ui.ui.BaseViewModel
import com.silpusitron.data.mechanism.Resource
import com.silpusitron.feature.settings.domain.ISettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val repository: ISettingsRepository
) : BaseViewModel<SettingsUiState, AccountUiAction>() {
    override val _state = MutableStateFlow(SettingsUiState())
    override fun doAction(action: AccountUiAction) {
        when(action){
            AccountUiAction.Logout -> viewModelScope.launch {
                repository.logout().collectLatest {
                    _state.update { state -> state.copy(logoutResult = it) }
                }
            }
        }
    }
}

sealed class AccountUiAction{
    data object Logout: AccountUiAction()
}

data class SettingsUiState(
    val logoutResult: Resource<String> = Resource.Idle()
)