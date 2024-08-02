package com.silpusitron.feature.auth.otp.ui

import androidx.lifecycle.viewModelScope
import com.silpusitron.shared.form.domain.model.InputTextData
import com.silpusitron.shared.form.domain.model.TextValidationType
import com.silpusitron.shared.form.domain.model.getMaxLength
import com.haltec.silpusitron.core.ui.ui.BaseViewModel
import com.silpusitron.data.mechanism.Resource
import com.silpusitron.feature.auth.common.domain.CheckSessionUseCase
import com.silpusitron.feature.auth.otp.domain.model.RequestOTPResult
import com.silpusitron.feature.auth.otp.domain.usecase.RequestOTPUseCase
import com.silpusitron.feature.auth.otp.domain.usecase.VerifyOTPUseCase
import com.silpusitron.shared.form.ui.BaseFormViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class OTPViewModel(
    private val checkSessionUseCase: CheckSessionUseCase,
    private val verifyOTPUseCase: VerifyOTPUseCase,
    private val requestOTPUseCase: RequestOTPUseCase
) : BaseFormViewModel<OTPState, OTPUiAction>(){
    override val _state = MutableStateFlow(OTPState())

    init {
        val otpVerificationResult = state.map { it.otpVerificationResult }.distinctUntilChanged()
        val requestOTPResult = state.map { it.requestOTPResult }.distinctUntilChanged()
        val combineFlow = combine(requestOTPResult, otpVerificationResult, ::Pair)
        viewModelScope.launch {
            combineFlow
                .debounce(500)
                .collect { (request, verification) ->
                    _state.update {state ->
                        state.copy(
                            isLoading = request is Resource.Loading || verification is Resource.Loading
                        )
                    }

                }
        }

        val isLoading = state.map { it.isLoading }.distinctUntilChanged()
        val timer = state.map { it.otpTimeOutInSec }.distinctUntilChanged()
        val timerOrLoadingFlow = combine(isLoading, timer, ::Pair)
        viewModelScope.launch {
            timerOrLoadingFlow
                .debounce(500)
                .collect { (isLoading, timer) ->
                    _state.update {state ->
                        state.copy(
                            enableRequestOTPAgain = !isLoading && timer == 0L
                        )
                    }
                }
        }

        val isValid = state.distinctUntilChangedBy { it.otpInput.isValid }.map { it.otpInput.isValid }
        val isValidOrLoadingFlow = combine(isLoading, isValid, ::Pair)
        viewModelScope.launch {
            isValidOrLoadingFlow
                .debounce(500)
                .collect { (isLoading, isValid) ->
                    _state.update {state ->
                        state.copy(
                            enableToVerifyOTP = !isLoading && isValid
                        )
                    }
                }
        }
    }

    override fun doAction(action: OTPUiAction) {
        when(action){
            is OTPUiAction.CheckSession -> {
                viewModelScope.launch {
                    checkSessionUseCase().collect{
                        _state.update { state ->
                            state.copy(isSessionValid = it)
                        }
                    }
                }
            }

            is OTPUiAction.SetOTP -> {
                setOTP(action.otp)
            }

            is OTPUiAction.Verify ->  {
                verifyOTP()
            }

            is OTPUiAction.RequestOTP -> {
                requestOTP()
            }

            is OTPUiAction.StartTimer -> {
                startTimer()
            }

            OTPUiAction.Retry -> {
                resetOTPVerificationToIdle()
            }
        }
    }


    private var otpMaxLength: Int? = null
    private fun setOTP(otp: String){

        if (otpMaxLength == null){
            // ensure it will only computed once
            otpMaxLength = state.value.otpInput.getMaxLength()
        }
        if (otp.length <= (otpMaxLength ?: 0)){
            val newOTPState = updateStateInputText(state.value.otpInput, otp)

            _state.update { state ->
                state.copy(
                    otp = otp,
                    otpInput = newOTPState
                )
            }
        }

    }


    private var firstRequest = true
    private fun requestOTP(){
        viewModelScope.launch {
            if (!firstRequest){
                _state.update { state ->
                    state.copy(
                        requestOTPResult = Resource.Loading()
                    )
                }
                delay(5000)
            }
            requestOTPUseCase().collectLatest {
                if(firstRequest) firstRequest = false
                _state.update { state ->
                    state.copy(
                        requestOTPResult = it,
                        otpTimeOutInSec = it.data?.otpTimeout ?: 0L,
                        otpTimeOutInString = """
                            ${(it.data?.otpTimeout ?: 0L) / 60}:${String.format("%02d", (it.data?.otpTimeout ?: 0L) % 60)}
                        """.trimIndent()
                    )
                }

                if(it is Resource.Success){
                    startTimer()
                }
            }
        }
    }

    private fun verifyOTP(){
        viewModelScope.launch {
            verifyOTPUseCase(state.value.otp).collectLatest {
                _state.update { state ->
                    state.copy(
                        otpVerificationResult = it
                    )
                }
            }
        }
    }

    private fun startTimer(){
        viewModelScope.launch {
            while ((state.value.otpTimeOutInSec ?: 0) > 0){
                delay(1000L)
                val remaining = (state.value.otpTimeOutInSec ?: 0) - 1
                _state.update { state ->
                    state.copy(
                        otpTimeOutInSec = remaining,
                        otpTimeOutInString = """
                            ${remaining / 60}:${String.format("%02d", remaining % 60)}
                        """.trimIndent()
                    )
                }
            }
        }
    }

    private fun resetOTPVerificationToIdle(){
        _state.update { state -> state.copy(otpVerificationResult = Resource.Idle()) }
    }
}

data class OTPState(
    val isSessionValid: Boolean? = null,
    val otp: String = "",
    val otpInput: InputTextData<TextValidationType, String> = InputTextData(
        inputName = "otp",
        value = "",
        validations = listOf(
            TextValidationType.Required,
            TextValidationType.ExactLength(6)
        )
    ),
    val requestOTPResult: Resource<RequestOTPResult> = Resource.Idle(),
    val otpVerificationResult: Resource<Boolean> = Resource.Idle(),
    val otpTimeOutInSec: Long? = null,
    val otpTimeOutInString: String? = null,
    val isLoading: Boolean = false,
    val enableRequestOTPAgain: Boolean = false,
    val enableToVerifyOTP: Boolean = false
)

sealed class OTPUiAction{
    data object CheckSession: OTPUiAction()
    data object RequestOTP: OTPUiAction()
    data class SetOTP(val otp: String): OTPUiAction()
    data object Verify: OTPUiAction()
    data object StartTimer: OTPUiAction()
    data object Retry: OTPUiAction()
}