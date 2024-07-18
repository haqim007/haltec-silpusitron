package com.haltec.silpusitron.shared.formprofile.ui

import androidx.lifecycle.viewModelScope
import com.haltec.silpusitron.data.mechanism.Resource
import com.haltec.silpusitron.shared.district.domain.usecase.GetDistrictsUseCase
import com.haltec.silpusitron.shared.form.domain.model.InputOptions
import com.haltec.silpusitron.shared.form.domain.model.InputTextData
import com.haltec.silpusitron.shared.form.domain.model.TextValidationType
import com.haltec.silpusitron.shared.form.ui.BaseFormViewModel
import com.haltec.silpusitron.shared.formprofile.domain.model.FormProfileInputKey
import com.haltec.silpusitron.shared.formprofile.domain.model.ProfileData
import com.haltec.silpusitron.shared.formprofile.domain.usecase.GetBloodTypeOptionsUseCase
import com.haltec.silpusitron.shared.formprofile.domain.usecase.GetEducationOptionsUseCase
import com.haltec.silpusitron.shared.formprofile.domain.usecase.GetFamRelationStatusOptionsUseCase
import com.haltec.silpusitron.shared.formprofile.domain.usecase.GetGenderOptionsUseCase
import com.haltec.silpusitron.shared.formprofile.domain.usecase.GetMarriageStatusOptionsUseCase
import com.haltec.silpusitron.shared.formprofile.domain.usecase.GetProffesionOptionsUseCase
import com.haltec.silpusitron.shared.formprofile.domain.usecase.GetProfileUseCase
import com.haltec.silpusitron.shared.formprofile.domain.usecase.GetReligionOptionsUseCase
import com.haltec.silpusitron.shared.formprofile.domain.usecase.GetSubDistrictsUseCase
import com.haltec.silpusitron.shared.formprofile.domain.usecase.ValidateAllInputUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FormProfileViewModel(
    private val getProfileUseCase: GetProfileUseCase,
    private val getGenderOptionsUseCase: GetGenderOptionsUseCase,
    private val getReligionOptionsUseCase: GetReligionOptionsUseCase,
    private val getBloodTypeOptionsUseCase: GetBloodTypeOptionsUseCase,
    private val getProfessionOptionsUseCase: GetProffesionOptionsUseCase,
    private val getFamRelationStatusOptionsUseCase: GetFamRelationStatusOptionsUseCase,
    private val getMarriageStatusOptionsUseCase: GetMarriageStatusOptionsUseCase,
    private val getEducationOptionsUseCase: GetEducationOptionsUseCase,
    private val getDistrictsUseCase: GetDistrictsUseCase,
    private val getSubDistrictsUseCase: GetSubDistrictsUseCase,
    private val validateAllInputUseCase: ValidateAllInputUseCase
) : BaseFormViewModel<FormProfileUiState, FormProfileUiAction>() {
    override val _state = MutableStateFlow(FormProfileUiState())
    override fun doAction(action: FormProfileUiAction) {
        when(action){
            FormProfileUiAction.GetProfileData -> {
                fetchProfileData()
            }
            is FormProfileUiAction.SetInput -> {
                setInput(action.input, action.value)
            }

            FormProfileUiAction.GetBloodTypeOptions -> fetchBloodTypeOptions()
            FormProfileUiAction.GetEducationOptions -> fetchEducationOptions()
            FormProfileUiAction.GetFamRelationStatusOptions -> fetchFamRelationStatusOptions()
            FormProfileUiAction.GetGenderOptions -> fetchGenderOptions()
            FormProfileUiAction.GetMarriageStatusOptions -> fetchMarriageStatusOptions()
            FormProfileUiAction.GetProfessionOptions -> fetchProfessionOptions()
            FormProfileUiAction.GetReligionOptions -> fetchReligionOptions()
            FormProfileUiAction.Submit -> submit()
            FormProfileUiAction.GetDistrictOptions -> fetchDistrictOptions()
            FormProfileUiAction.GetSubDistrictOptions -> fetchSubDistrictOptions()
            is FormProfileUiAction.SaveInputViewCoordinateY -> saveInputViewCoordinateY(
                action.key,
                action.coordinateY
            )
            FormProfileUiAction.ResetFirstErrorInputKey -> resetFirstErrorKey()
            FormProfileUiAction.ResetIsAllValidState -> _state.update { state ->
                state.copy(isAllValid = null)
            }
        }
    }

    private fun resetFirstErrorKey(){
        _state.update { state -> state.copy(firstErrorInputKey = null) }
    }

    private fun saveInputViewCoordinateY(key: FormProfileInputKey, coordinateY: Float){
        val newCoordinateInputs = state.value.inputsCoordinateY.toMutableMap()
        newCoordinateInputs[key] = coordinateY - 700
        _state.update { state ->
            state.copy(
                inputsCoordinateY = newCoordinateInputs
            )
        }
    }

    private fun submit(){
        val validateInputs = validateAllInputUseCase(state.value.inputs)
        _state.update { state -> state.copy(isAllValid = validateInputs.isAllValid) }
        if (!validateInputs.isAllValid){
            _state.update { state ->
                state.copy(
                    inputs = validateInputs.input,
                    firstErrorInputKey = validateInputs.firstErrorInputKey
                )
            }

            return
        }
    }

    private fun fetchProfileData(){
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

    private fun fetchDistrictOptions(){
        viewModelScope.launch {
            getDistrictsUseCase().collectLatest { res ->
                if (res is Resource.Success){
                    _state.update { state ->
                        val inputsState = state.inputs.toMutableMap()
                        inputsState[FormProfileInputKey.DISTRICT]?.let {
                            inputsState[FormProfileInputKey.DISTRICT] = it.copy(
                                options = res.data?.options
                            )
                        }
                        state.copy(
                            districtOptions = res,
                            inputs = inputsState
                        )
                    }
                }else{
                    _state.update { state ->
                        val inputsState = state.inputs.toMutableMap()
                        inputsState[FormProfileInputKey.DISTRICT]?.let {
                            inputsState[FormProfileInputKey.DISTRICT] = it.copy(
                                message = res.message
                            )
                        }
                        state.copy(
                            districtOptions = res,
                            inputs = inputsState
                        )
                    }
                }

            }
        }
    }

    private fun fetchSubDistrictOptions(){
        viewModelScope.launch {
            getSubDistrictsUseCase(
                state.value.inputs[FormProfileInputKey.DISTRICT]?.value
            ).collectLatest { res ->
                _state.update { state ->
                    if (res is Resource.Success){
                        val inputsState = state.inputs.toMutableMap()
                        inputsState[FormProfileInputKey.SUB_DISTRICT]?.let {
                            inputsState[FormProfileInputKey.SUB_DISTRICT] = it.copy(
                                options = res.data?.options
                            )
                        }
                        state.copy(
                            subDistrictOptions = res,
                            inputs = inputsState
                        )
                    }else{
                        val inputsState = state.inputs.toMutableMap()
                        inputsState[FormProfileInputKey.SUB_DISTRICT]?.let {
                            inputsState[FormProfileInputKey.SUB_DISTRICT] = it.copy(
                                message = res.message
                            )
                        }
                        state.copy(
                            subDistrictOptions = res,
                            inputs = inputsState
                        )
                    }
                }
            }
        }
    }

    private fun fetchGenderOptions(){
        viewModelScope.launch {
            getGenderOptionsUseCase().collectLatest { res ->
                _state.update { state ->
                    if (res is Resource.Success){
                        val inputsState = state.inputs.toMutableMap()
                        inputsState[FormProfileInputKey.GENDER]?.let {
                            inputsState[FormProfileInputKey.GENDER] = it.copy(
                                options = res.data?.options
                            )
                        }
                        state.copy(
                            genderOptions = res,
                            inputs = inputsState
                        )
                    }else{
                        val inputsState = state.inputs.toMutableMap()
                        inputsState[FormProfileInputKey.GENDER]?.let {
                            inputsState[FormProfileInputKey.GENDER] = it.copy(
                                message = res.message
                            )
                        }
                        state.copy(
                            genderOptions = res,
                            inputs = inputsState
                        )
                    }
                }
            }
        }
    }

    private fun fetchProfessionOptions(){
        viewModelScope.launch {
            getProfessionOptionsUseCase().collectLatest { res ->
                _state.update { state ->
                    if (res is Resource.Success){
                        val inputsState = state.inputs.toMutableMap()
                        inputsState[FormProfileInputKey.PROFESSION]?.let {
                            inputsState[FormProfileInputKey.PROFESSION] = it.copy(
                                options = res.data?.options
                            )
                        }
                        state.copy(
                            professionOptions = res,
                            inputs = inputsState
                        )
                    }else{
                        val inputsState = state.inputs.toMutableMap()
                        inputsState[FormProfileInputKey.PROFESSION]?.let {
                            inputsState[FormProfileInputKey.PROFESSION] = it.copy(
                                message = res.message
                            )
                        }
                        state.copy(
                            professionOptions = res,
                            inputs = inputsState
                        )
                    }
                }
            }
        }
    }

    private fun fetchMarriageStatusOptions(){
        viewModelScope.launch {
            getMarriageStatusOptionsUseCase().collectLatest { res ->
                _state.update { state ->
                    if (res is Resource.Success){
                        val inputsState = state.inputs.toMutableMap()
                        inputsState[FormProfileInputKey.MARRIAGE_STATUS]?.let {
                            inputsState[FormProfileInputKey.MARRIAGE_STATUS] = it.copy(
                                options = res.data?.options
                            )
                        }
                        state.copy(
                            marriageStatusOptions = res,
                            inputs = inputsState
                        )
                    }else{
                        val inputsState = state.inputs.toMutableMap()
                        inputsState[FormProfileInputKey.MARRIAGE_STATUS]?.let {
                            inputsState[FormProfileInputKey.MARRIAGE_STATUS] = it.copy(
                                message = res.message
                            )
                        }
                        state.copy(
                            marriageStatusOptions = res,
                            inputs = inputsState
                        )
                    }
                }
            }
        }
    }

    private fun fetchReligionOptions(){
        viewModelScope.launch {
            getReligionOptionsUseCase().collectLatest { res ->
                _state.update { state ->
                    if (res is Resource.Success){
                        val inputsState = state.inputs.toMutableMap()
                        inputsState[FormProfileInputKey.RELIGION]?.let {
                            inputsState[FormProfileInputKey.RELIGION] = it.copy(
                                options = res.data?.options
                            )
                        }
                        state.copy(
                            religionOptions = res,
                            inputs = inputsState
                        )
                    }else{
                        val inputsState = state.inputs.toMutableMap()
                        inputsState[FormProfileInputKey.RELIGION]?.let {
                            inputsState[FormProfileInputKey.RELIGION] = it.copy(
                                message = res.message
                            )
                        }
                        state.copy(
                            religionOptions = res,
                            inputs = inputsState
                        )
                    }
                }
            }
        }
    }

    private fun fetchEducationOptions(){
        viewModelScope.launch {
            getEducationOptionsUseCase().collectLatest { res ->
                _state.update { state ->
                    if (res is Resource.Success){
                        val inputsState = state.inputs.toMutableMap()
                        inputsState[FormProfileInputKey.EDUCATION]?.let {
                            inputsState[FormProfileInputKey.EDUCATION] = it.copy(
                                options = res.data?.options
                            )
                        }
                        state.copy(
                            educationOptions = res,
                            inputs = inputsState
                        )
                    }else{
                        val inputsState = state.inputs.toMutableMap()
                        inputsState[FormProfileInputKey.EDUCATION]?.let {
                            inputsState[FormProfileInputKey.EDUCATION] = it.copy(
                                message = res.message
                            )
                        }
                        state.copy(
                            educationOptions = res,
                            inputs = inputsState
                        )
                    }
                }
            }
        }
    }

    private fun fetchBloodTypeOptions(){
        viewModelScope.launch {
            getBloodTypeOptionsUseCase().collectLatest { res ->
                _state.update { state ->
                    if (res is Resource.Success){
                        val inputsState = state.inputs.toMutableMap()
                        inputsState[FormProfileInputKey.BLOOD_TYPE]?.let {
                            inputsState[FormProfileInputKey.BLOOD_TYPE] = it.copy(
                                options = res.data?.options
                            )
                        }
                        state.copy(
                            bloodTypeOptions = res,
                            inputs = inputsState
                        )
                    }else{
                        val inputsState = state.inputs.toMutableMap()
                        inputsState[FormProfileInputKey.BLOOD_TYPE]?.let {
                            inputsState[FormProfileInputKey.BLOOD_TYPE] = it.copy(
                                message = res.message
                            )
                        }
                        state.copy(
                            bloodTypeOptions = res,
                            inputs = inputsState
                        )
                    }
                }
            }
        }
    }

    private fun fetchFamRelationStatusOptions(){
        viewModelScope.launch {
            getFamRelationStatusOptionsUseCase().collectLatest { res ->
                _state.update { state ->
                    if (res is Resource.Success){
                        val inputsState = state.inputs.toMutableMap()
                        inputsState[FormProfileInputKey.FAMILY_RELATION]?.let {
                            inputsState[FormProfileInputKey.FAMILY_RELATION] = it.copy(
                                options = res.data?.options
                            )
                        }
                        state.copy(
                            famRelationStatusOptions = res,
                            inputs = inputsState
                        )
                    }else{
                        val inputsState = state.inputs.toMutableMap()
                        inputsState[FormProfileInputKey.FAMILY_RELATION]?.let {
                            inputsState[FormProfileInputKey.FAMILY_RELATION] = it.copy(
                                message = res.message
                            )
                        }
                        state.copy(
                            famRelationStatusOptions = res,
                            inputs = inputsState
                        )
                    }
                }
            }
        }
    }

    private fun setInput(key: FormProfileInputKey, value: String){
        val inputState = state.value.inputs[key]
        if (inputState != null){

            val newInput = updateStateInputText(
                inputState,
                value
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

}

sealed class FormProfileUiAction{
    data object GetProfileData: FormProfileUiAction()
    data class SetInput(val input: FormProfileInputKey, val value: String): FormProfileUiAction()
    data object GetGenderOptions: FormProfileUiAction()
    data object GetReligionOptions: FormProfileUiAction()
    data object GetBloodTypeOptions: FormProfileUiAction()
    data object GetFamRelationStatusOptions: FormProfileUiAction()
    data object GetEducationOptions: FormProfileUiAction()
    data object GetProfessionOptions: FormProfileUiAction()
    data object GetMarriageStatusOptions: FormProfileUiAction()
    data object Submit: FormProfileUiAction()
    data object GetDistrictOptions: FormProfileUiAction()
    data object GetSubDistrictOptions: FormProfileUiAction()
    data class SaveInputViewCoordinateY(
        val key: FormProfileInputKey,
        val coordinateY: Float
    ): FormProfileUiAction()
    data object ResetFirstErrorInputKey: FormProfileUiAction()
    data object ResetIsAllValidState: FormProfileUiAction()
}

/**
 * Form profile ui state
 *
 * @property profileData Current ProfileData
 * @property inputs Holds user input
 * @property genderOptions
 * @property religionOptions
 * @property bloodTypeOptions
 * @property famRelationStatusOptions
 * @property educationOptions
 * @property professionOptions
 * @property marriageStatusOptions
 * @property districtOptions
 * @property subDistrictOptions
 * @property inputsCoordinateY Holds coordinate Y of all input fields.
 * @property firstErrorInputKey Key to be scrolled to.
 * Used for scrolling to certain input on error when user click submit
 * @constructor Create empty Form profile ui state
 */
data class FormProfileUiState(
    val profileData: Resource<ProfileData> = Resource.Idle(),
    val inputs: Map<FormProfileInputKey, InputTextData<TextValidationType, String>> = mapOf(),
    val genderOptions: Resource<InputOptions> = Resource.Idle(),
    val religionOptions: Resource<InputOptions> = Resource.Idle(),
    val bloodTypeOptions: Resource<InputOptions> = Resource.Idle(),
    val famRelationStatusOptions: Resource<InputOptions> = Resource.Idle(),
    val educationOptions: Resource<InputOptions> = Resource.Idle(),
    val professionOptions: Resource<InputOptions> = Resource.Idle(),
    val marriageStatusOptions: Resource<InputOptions> = Resource.Idle(),
    val districtOptions: Resource<InputOptions> = Resource.Idle(),
    val subDistrictOptions: Resource<InputOptions> = Resource.Idle(),
    val inputsCoordinateY: Map<FormProfileInputKey, Float> = mapOf(),
    val firstErrorInputKey: FormProfileInputKey? = null,
    val isAllValid: Boolean? = null
)

