package com.haltec.silpusitron.feature.auth.login.ui

import androidx.lifecycle.viewModelScope
import com.haltec.silpusitron.core.domain.model.InputTextData
import com.haltec.silpusitron.core.domain.model.TextValidationType
import com.haltec.silpusitron.core.domain.model.validate
import com.haltec.silpusitron.core.ui.ui.BaseViewModel
import com.haltec.silpusitron.data.mechanism.Resource
import com.haltec.silpusitron.feature.auth.common.domain.UserType
import com.haltec.silpusitron.feature.auth.login.domain.model.LoginInputData
import com.haltec.silpusitron.feature.auth.login.domain.usecase.LoginUseCase
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class LoginViewModel(
    private val useCase: LoginUseCase
): BaseViewModel<LoginUiState, LoginUiAction>() {
    override val _state = MutableStateFlow(LoginUiState())

    init {
        val username = state.distinctUntilChangedBy { it.username }.map { it.usernameInput }
        val password = state.distinctUntilChangedBy { it.password }.map { it.passwordInput }
        val captcha = state.distinctUntilChangedBy { it.captcha }.map { it.captchaInput }
        val combineFlow = combine(username, password, captcha, ::Triple)
        viewModelScope.launch {
            combineFlow
                .debounce(500)
                .collect { (username, password, captcha) ->
                    _state.update {state ->
                        state.copy(
                            enableLogin =
                            (password.isValid && password.everChanged) &&
                                    (username.isValid && username.everChanged) &&
                                    (captcha.isValid && captcha.everChanged)
                        )
                    }

                }
        }
    }

    override fun doAction(action: LoginUiAction){
        when(action){
            is LoginUiAction.SetUsername -> {
                setUsername(action.username)
            }
            is LoginUiAction.SetPassword -> {
                setPassword(action.password)
            }
            is LoginUiAction.SetCaptchaLoading ->{
                setCaptchaLoading()
            }
            is LoginUiAction.SetCaptchaResult -> {
                setCaptchaValid(action.isValid, action.token, action.error)
            }
            is LoginUiAction.Submit -> {
                submit(action.userType)
            }
            is LoginUiAction.HidePassword -> {
                _state.update { state -> state.copy(passwordHidden = true) }
            }
            is LoginUiAction.ShowPassword -> {
                _state.update { state -> state.copy(passwordHidden = false) }
            }
        }
    }

    private fun setUsername(username: String){
        val usernameState = state.value.usernameInput
        usernameState.setValue(username)
        usernameState.validate()
        _state.update { state ->
            state.copy(
                usernameInput = usernameState,
                username = username
            )
        }
    }

    private fun setPassword(password: String){
        val passwordState = state.value.passwordInput
        passwordState.setValue(password)
        passwordState.validate()
        _state.update { state ->
            state.copy(
                passwordInput = passwordState,
                password = password
            )
        }
    }

    private fun setCaptchaLoading(){
        viewModelScope.launch {
            _state.update { state ->
                state.copy(
                    captcha = Resource.Loading()
                )
            }
        }
    }

    private fun setCaptchaValid(isValid: Boolean, token: String? = null, error: Throwable? = null) {
        viewModelScope.launch {
            val captchaState = state.value.captchaInput
            if (token != null) {
                captchaState.setValue(token)
            }
            captchaState.validate()
            if (isValid && token != null){
                _state.update { state ->
                    state.copy(
                        captcha = Resource.Success(data = token),
                        captchaInput = state.captchaInput.copy(
                            value = token,
                            validationError = null,
                            message = null
                        )
                    )
                }
            }
            else if(error != null){
                _state.update { state ->
                    state.copy(
                        captcha = Resource.Error(message = error.localizedMessage ?: "Unknown error"),
                        captchaInput = captchaState.copy(
                            value = ""
                        )
                    )
                }
            }
        }
    }

    private fun submit(userType: UserType) {
        if (!state.value.enableLogin) return
        viewModelScope.launch {
            useCase(
                state.value.usernameInput,
                state.value.passwordInput,
                state.value.captchaInput,
                userType
            )
                .collectLatest {
                    _state.update { state ->
                        state.copy(
                            loginResult = it,
                            usernameInput = it.data?.username ?: state.usernameInput,
                            passwordInput = it.data?.password ?: state.passwordInput,
                            captchaInput = it.data?.captcha ?: state.captchaInput
                        )
                    }
                }
        }
    }

}

data class LoginUiState(
    val username: String = "",
    val usernameInput: InputTextData<TextValidationType, String> = InputTextData(
        inputName = "username",
        message = null,
        value = "",
        validations = listOf(TextValidationType.Required),
        validationError = null
    ),
    val password: String = "",
    val passwordInput: InputTextData<TextValidationType, String> = InputTextData(
        inputName = "password",
        message = null,
        value = "",
        validations = listOf(TextValidationType.Required, TextValidationType.MinLength(7)),
        validationError = null
    ),
    val captcha: Resource<String> = Resource.Idle(),
    val captchaInput: InputTextData<TextValidationType, String> = InputTextData(
        inputName = "g-recaptcha-response",
        message = null,
        value = "",
        validations = listOf(TextValidationType.Required),
        validationError = null
    ),
    val enableLogin: Boolean = false,
    val loginResult: Resource<LoginInputData> = Resource.Idle(),
    val passwordHidden: Boolean = true
)

sealed class LoginUiAction{
    data class SetUsername(val username: String): LoginUiAction()
    data class SetPassword(val password: String): LoginUiAction()
    data class SetCaptchaResult(
        val isValid: Boolean,
        val token: String? = null,
        val error: Throwable? = null
    ): LoginUiAction()
    data object SetCaptchaLoading: LoginUiAction()
    data class Submit(val userType: UserType): LoginUiAction()
    data object HidePassword: LoginUiAction()
    data object ShowPassword: LoginUiAction()
}