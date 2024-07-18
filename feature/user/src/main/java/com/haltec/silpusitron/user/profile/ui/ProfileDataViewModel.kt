package com.haltec.silpusitron.user.profile.ui

import androidx.lifecycle.viewModelScope
import com.haltec.silpusitron.shared.form.domain.model.InputTextData
import com.haltec.silpusitron.shared.form.domain.model.TextValidationType
import com.haltec.silpusitron.data.mechanism.Resource
import com.haltec.silpusitron.shared.formprofile.domain.model.FormProfileInputKey
import com.haltec.silpusitron.shared.formprofile.domain.model.ProfileData
import com.haltec.silpusitron.shared.form.domain.model.InputOptions
import com.haltec.silpusitron.shared.formprofile.domain.usecase.GetBloodTypeOptionsUseCase
import com.haltec.silpusitron.shared.district.domain.usecase.GetDistrictsUseCase
import com.haltec.silpusitron.shared.form.ui.BaseFormViewModel
import com.haltec.silpusitron.shared.formprofile.data.dummy.formProfileInputDummy
import com.haltec.silpusitron.shared.formprofile.domain.usecase.GetEducationOptionsUseCase
import com.haltec.silpusitron.shared.formprofile.domain.usecase.GetFamRelationStatusOptionsUseCase
import com.haltec.silpusitron.shared.formprofile.domain.usecase.GetGenderOptionsUseCase
import com.haltec.silpusitron.shared.formprofile.domain.usecase.GetMarriageStatusOptionsUseCase
import com.haltec.silpusitron.shared.formprofile.domain.usecase.GetProffesionOptionsUseCase
import com.haltec.silpusitron.shared.formprofile.domain.usecase.GetProfileUseCase
import com.haltec.silpusitron.shared.formprofile.domain.usecase.GetReligionOptionsUseCase
import com.haltec.silpusitron.shared.formprofile.domain.usecase.GetSubDistrictsUseCase
import com.haltec.silpusitron.user.profile.domain.usecase.SubmitProfileUseCase
import com.haltec.silpusitron.shared.formprofile.domain.usecase.ValidateAllInputUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileDataViewModel(
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
    private val validateAllInputUseCase: ValidateAllInputUseCase,
    private val submitProfileUseCase: SubmitProfileUseCase
) : BaseFormViewModel<ProfileDataUiState, ProfileDataUiAction>() {
    override val _state = MutableStateFlow(ProfileDataUiState())
    override fun doAction(action: ProfileDataUiAction) {
        when(action){
            ProfileDataUiAction.GetProfileData -> {
                fetchProfileData()
            }
            is ProfileDataUiAction.SetInput -> {
                setInput(action.input, action.value)
            }

            ProfileDataUiAction.GetBloodTypeOptions -> fetchBloodTypeOptions()
            ProfileDataUiAction.GetEducationOptions -> fetchEducationOptions()
            ProfileDataUiAction.GetFamRelationStatusOptions -> fetchFamRelationStatusOptions()
            ProfileDataUiAction.GetGenderOptions -> fetchGenderOptions()
            ProfileDataUiAction.GetMarriageStatusOptions -> fetchMarriageStatusOptions()
            ProfileDataUiAction.GetProfessionOptions -> fetchProfessionOptions()
            ProfileDataUiAction.GetReligionOptions -> fetchReligionOptions()
            ProfileDataUiAction.Submit -> submit()
            ProfileDataUiAction.GetDistrictOptions -> fetchDistrictOptions()
            ProfileDataUiAction.GetSubDistrictOptions -> fetchSubDistrictOptions()
            is ProfileDataUiAction.SaveInputViewCoordinateY -> saveInputViewCoordinateY(
                action.key,
                action.coordinateY
            )
            ProfileDataUiAction.ResetFirstErrorInputKey -> resetFirstErrorKey()
            ProfileDataUiAction.ResetSubmitResult -> resetSubmitResult()
            is ProfileDataUiAction.SetDummyState -> {
                _state.update { action.state }
            }
        }
    }

    private fun resetSubmitResult() {
        _state.update { state -> state.copy(submitResult = Resource.Idle()) }
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
        if (!validateInputs.isAllValid){
            _state.update { state ->
                state.copy(
                    inputs = validateInputs.input,
                    firstErrorInputKey = validateInputs.firstErrorInputKey
                )
            }
            return
        }

        viewModelScope.launch {
            with(state.value){
                submitProfileUseCase(profileData.data!!, inputs).collectLatest {
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

/**
 * Profile data ui action
 *
 * @constructor Create empty Profile data ui action
 */
sealed class ProfileDataUiAction{
    data object GetProfileData: ProfileDataUiAction()
    data class SetInput(val input: FormProfileInputKey, val value: String): ProfileDataUiAction()
    data object GetGenderOptions: ProfileDataUiAction()
    data object GetReligionOptions: ProfileDataUiAction()
    data object GetBloodTypeOptions: ProfileDataUiAction()
    data object GetFamRelationStatusOptions: ProfileDataUiAction()
    data object GetEducationOptions: ProfileDataUiAction()
    data object GetProfessionOptions: ProfileDataUiAction()
    data object GetMarriageStatusOptions: ProfileDataUiAction()
    data object Submit: ProfileDataUiAction()
    data object GetDistrictOptions: ProfileDataUiAction()
    data object GetSubDistrictOptions: ProfileDataUiAction()
    data class SaveInputViewCoordinateY(val key: FormProfileInputKey, val coordinateY: Float): ProfileDataUiAction()
    data object ResetFirstErrorInputKey: ProfileDataUiAction()
    data object ResetSubmitResult: ProfileDataUiAction()

    /**
     * Set dummy state for preview
     *
     * @constructor Create empty Profile data ui action
     */
    data class SetDummyState(val state: ProfileDataUiState): ProfileDataUiAction()
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
data class ProfileDataUiState(
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
    val submitResult: Resource<ProfileData> = Resource.Idle()
)

