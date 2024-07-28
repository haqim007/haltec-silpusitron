package com.haltec.silpusitron.feature.updateprofileofficer.ui

import androidx.lifecycle.viewModelScope
import com.haltec.silpusitron.data.mechanism.Resource
import com.haltec.silpusitron.shared.form.domain.model.InputTextData
import com.haltec.silpusitron.shared.form.domain.model.TextValidationType
import com.haltec.silpusitron.shared.form.domain.model.validate
import com.haltec.silpusitron.shared.form.ui.BaseFormViewModel
import com.haltec.silpusitron.feature.updateprofileofficer.domain.FormProfileOfficerInputKey
import com.haltec.silpusitron.feature.updateprofileofficer.domain.GetProfileOfficerUseCase
import com.haltec.silpusitron.feature.updateprofileofficer.domain.ProfileOfficerData
import com.haltec.silpusitron.feature.updateprofileofficer.domain.ProfileOfficerDataDummy
import com.haltec.silpusitron.feature.updateprofileofficer.domain.SubmitProfileOfficerUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UpdateProfileOfficerViewModel(
    private val getProfileUseCase: GetProfileOfficerUseCase,
    private val submitProfileUseCase: SubmitProfileOfficerUseCase
): BaseFormViewModel<ProfileOfficerUiState, ProfileOfficerUiAction>() {
    override val _state = MutableStateFlow(ProfileOfficerUiState())
    override fun doAction(action: ProfileOfficerUiAction) {
        when(action){
            ProfileOfficerUiAction.GetProfileOfficerData -> fetchProfileOfficerData()
            is ProfileOfficerUiAction.SetInput -> setInput(action.input, action.value)
            ProfileOfficerUiAction.ValidateAll -> validateAll()
            is ProfileOfficerUiAction.SaveInputViewCoordinateY -> saveInputViewCoordinateY(
                action.key,
                action.coordinateY
            )
            ProfileOfficerUiAction.ResetFirstErrorInputKey -> resetFirstErrorKey()
            ProfileOfficerUiAction.ResetIsAllValidState -> _state.update { state ->
                state.copy(isAllValid = null)
            }
            ProfileOfficerUiAction.Submit -> submit()
            ProfileOfficerUiAction.ResetSubmitState -> _state.update { state ->
                state.copy(submitResult = Resource.Idle())
            }

            is ProfileOfficerUiAction.SetDummyState -> _state.update { action.state }
        }
    }

    private fun fetchProfileOfficerData(){
        viewModelScope.launch {
            getProfileUseCase().collectLatest { res ->
                _state.update { state ->
                    state.copy(
                        profileData = res
                    )
                }
                if (res is Resource.Success){
                    _state.update { state ->
                        state.copy(
                            inputs = res.data!!.input
                        )
                    }
                }
            }
        }
    }

    private fun submit() {
        if(!validateAll()) return
        viewModelScope.launch {
            submitProfileUseCase(
                state.value.profileData.data!!,
                state.value.inputs
            ).collectLatest {
                if (it is Resource.Success){
                    _state.update { state ->
                        state.copy(
                            submitResult = it,
                            inputs = it.data!!.input
                        )
                    }
                }
                else if(it is Resource.Error){
                    val errorInputs = it.data?.input
                    val currentInput = state.value.inputs.toMutableMap()
                    errorInputs?.forEach { (key, input) ->
                        currentInput[key]?.let { currentInputItem ->
                            currentInput[key] = currentInputItem.copy(
                                message = input.message
                            )
                        }
                    }
                    _state.update { state ->
                        state.copy(
                            submitResult = it,
                            inputs = currentInput,
                            firstErrorInputKey = currentInput.entries.find { !it.value.isValid }?.key,
                        )
                    }
                }
                else{
                    _state.update { state ->
                        state.copy(
                            submitResult = it,
                        )
                    }
                }
            }
        }
    }

    private fun setInput(key: FormProfileOfficerInputKey, value: String){
        val inputState = state.value.inputs[key]
        if (inputState != null){

            val newInput = updateStateInputText(
                inputState,
                if (key == FormProfileOfficerInputKey.PHONE_NUMBER){
                    if (value.startsWith("62")){
                        value.replaceFirst("^62".toRegex(), "")
                    }
                    else if (value.startsWith("08")){
                        value.replaceFirst("^08".toRegex(), "8")
                    }
                    else value
                }else{
                    value
                }
            )

            val newInputs = state.value.inputs.toMutableMap()
            newInputs[key] = newInput

            _state.update { state ->
                state.copy(
                    inputs = newInputs.toMap(),
                )
            }
        }

    }

    private fun resetFirstErrorKey(){
        _state.update { state -> state.copy(firstErrorInputKey = null) }
    }

    private fun saveInputViewCoordinateY(key: FormProfileOfficerInputKey, coordinateY: Float){
        val newCoordinateInputs = state.value.inputsCoordinateY.toMutableMap()
        newCoordinateInputs[key] = coordinateY - 700
        _state.update { state ->
            state.copy(
                inputsCoordinateY = newCoordinateInputs
            )
        }
    }

    private fun validateAll(): Boolean{

        var isAllValid = true
        var firstErrorInputKey: FormProfileOfficerInputKey? = null

        val newInputs = state.value.inputs.toMutableMap()
        newInputs.forEach {
            newInputs[it.key] = it.value.validate()

            if (isAllValid && newInputs[it.key]?.isValid == false) {
                isAllValid = false
                if (firstErrorInputKey == null){
                    firstErrorInputKey = it.key
                }
            }

            _state.update { state ->
                state.copy(
                    inputs = newInputs,
                )
            }
        }

        _state.update { state ->
            state.copy(
                firstErrorInputKey = firstErrorInputKey
            )
        }

        return isAllValid
    }
}

data class ProfileOfficerUiState(
    val profileData: Resource<ProfileOfficerData> = Resource.Idle(),
    val inputs: Map<FormProfileOfficerInputKey, InputTextData<TextValidationType, String>> = mapOf(),
    val inputsCoordinateY: Map<FormProfileOfficerInputKey, Float> = mapOf(),
    val firstErrorInputKey: FormProfileOfficerInputKey? = null,
    val isAllValid: Boolean? = null,
    val submitResult: Resource<ProfileOfficerData> = Resource.Idle()
)

sealed class ProfileOfficerUiAction{
    data object GetProfileOfficerData: ProfileOfficerUiAction()
    data class SetInput(val input: FormProfileOfficerInputKey, val value: String): ProfileOfficerUiAction()
    data object ValidateAll: ProfileOfficerUiAction()
    data object Submit: ProfileOfficerUiAction()
    data class SaveInputViewCoordinateY(
        val key: FormProfileOfficerInputKey,
        val coordinateY: Float
    ): ProfileOfficerUiAction()

    data class SetDummyState(val state: ProfileOfficerUiState) : ProfileOfficerUiAction()

    data object ResetFirstErrorInputKey: ProfileOfficerUiAction()
    data object ResetIsAllValidState: ProfileOfficerUiAction()
    data object ResetSubmitState: ProfileOfficerUiAction()
}

internal fun ProfileOfficerUiStateDummy() = ProfileOfficerUiState(
    profileData = Resource.Success(ProfileOfficerDataDummy),
    inputs = ProfileOfficerDataDummy.input,
)