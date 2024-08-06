package com.silpusitron.feature.auth.phonenumberupdate.ui

import androidx.lifecycle.viewModelScope
import com.silpusitron.core.ui.ui.BaseViewModel
import com.silpusitron.data.mechanism.Resource
import com.silpusitron.feature.auth.phonenumberupdate.domain.usecase.PhoneNumberUpdateUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PhoneNumberUpdateViewModel(
    private val phoneNumberUpdateUseCase: PhoneNumberUpdateUseCase
) : BaseViewModel<UPNUiState, UPNUiAction>() {
    override val _state =  MutableStateFlow(UPNUiState())
    override fun doAction(action: UPNUiAction) {
        when(action){
            is UPNUiAction.Submit -> {
                viewModelScope.launch {
                    phoneNumberUpdateUseCase(action.phoneNumber)
                        .collectLatest{
                            _state.update { state -> state.copy(submitResult = it) }
                        }
                }
            }
        }
    }


}

data class UPNUiState(
    val submitResult: Resource<String> = Resource.Idle()
)

sealed class UPNUiAction{
    data class Submit(val phoneNumber: String): UPNUiAction()
}