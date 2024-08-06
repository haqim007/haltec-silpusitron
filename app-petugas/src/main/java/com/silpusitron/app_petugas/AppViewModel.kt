package com.silpusitron.app_petugas

import androidx.lifecycle.viewModelScope
import com.silpusitron.core.ui.ui.BaseViewModel
import com.silpusitron.feature.auth.common.domain.IAuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class AppViewModel(
    private val authRepository: IAuthRepository
) : BaseViewModel<AppState, AppUiAction>(){
    override val _state = MutableStateFlow(AppState())
    override fun doAction(action: AppUiAction) {
        when(action){
            is AppUiAction.CheckSession -> {
                viewModelScope.launch {
                   authRepository.checkSession().collect{
                       _state.update { state ->
                           state.copy(isSessionValid = it)
                       }
                   }
                }
            }
        }
    }
}

data class AppState(
    val isSessionValid: Boolean? = null
)

sealed class AppUiAction{
    data object CheckSession: AppUiAction()
}

