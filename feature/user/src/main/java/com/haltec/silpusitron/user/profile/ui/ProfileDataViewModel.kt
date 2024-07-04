package com.haltec.silpusitron.user.profile.ui

import androidx.lifecycle.viewModelScope
import com.haltec.silpusitron.core.domain.model.InputTextData
import com.haltec.silpusitron.core.domain.model.TextValidationType
import com.haltec.silpusitron.core.domain.model.validate
import com.haltec.silpusitron.core.ui.ui.BaseViewModel
import com.haltec.silpusitron.data.mechanism.Resource
import com.haltec.silpusitron.user.profile.domain.model.FormProfileInputKey
import com.haltec.silpusitron.user.profile.domain.model.ProfileData
import com.haltec.silpusitron.user.profile.domain.model.ProfileDataDummy
import com.haltec.silpusitron.user.profile.domain.model.ProfileInputOptions
import com.haltec.silpusitron.user.profile.domain.usecase.GetBloodTypeOptionsUseCase
import com.haltec.silpusitron.user.profile.domain.usecase.GetDistrictsUseCase
import com.haltec.silpusitron.user.profile.domain.usecase.GetEducationOptionsUseCase
import com.haltec.silpusitron.user.profile.domain.usecase.GetFamRelationStatusOptionsUseCase
import com.haltec.silpusitron.user.profile.domain.usecase.GetGenderOptionsUseCase
import com.haltec.silpusitron.user.profile.domain.usecase.GetMarriageStatusOptionsUseCase
import com.haltec.silpusitron.user.profile.domain.usecase.GetProffesionOptionsUseCase
import com.haltec.silpusitron.user.profile.domain.usecase.GetProfileUseCase
import com.haltec.silpusitron.user.profile.domain.usecase.GetReligionOptionsUseCase
import com.haltec.silpusitron.user.profile.domain.usecase.GetSubDistrictsUseCase
import com.haltec.silpusitron.user.profile.domain.usecase.SubmitProfileUseCase
import kotlinx.coroutines.delay
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
    private val submitProfileUseCase: SubmitProfileUseCase
) : BaseViewModel<FormProfileUiState, FormProfileUiAction>() {
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
            FormProfileUiAction.ResetSubmitResult -> resetSubmitResult()
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
        val validatedInput = state.value.inputs.toMutableMap()
        state.value.inputs.forEach {
            validatedInput[it.key] = it.value.validate()
        }
        val isAllValid: Boolean = validatedInput.entries.find { !it.value.isValid } == null

        if (!isAllValid){
            _state.update { state ->
                state.copy(
                    inputs = validatedInput,
                    firstErrorInputKey = validatedInput.entries.find { !it.value.isValid }?.key
                )
            }
            return
        }

        viewModelScope.launch {
            with(state.value){
                submitProfileUseCase(profileData.data!!, inputs).collectLatest {
                    _state.update { state -> state.copy(submitResult = it) }
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
    data class SaveInputViewCoordinateY(val key: FormProfileInputKey, val coordinateY: Float): FormProfileUiAction()
    data object ResetFirstErrorInputKey: FormProfileUiAction()
    data object ResetSubmitResult: FormProfileUiAction()
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
    val genderOptions: Resource<ProfileInputOptions> = Resource.Idle(),
    val religionOptions: Resource<ProfileInputOptions> = Resource.Idle(),
    val bloodTypeOptions: Resource<ProfileInputOptions> = Resource.Idle(),
    val famRelationStatusOptions: Resource<ProfileInputOptions> = Resource.Idle(),
    val educationOptions: Resource<ProfileInputOptions> = Resource.Idle(),
    val professionOptions: Resource<ProfileInputOptions> = Resource.Idle(),
    val marriageStatusOptions: Resource<ProfileInputOptions> = Resource.Idle(),
    val districtOptions: Resource<ProfileInputOptions> = Resource.Idle(),
    val subDistrictOptions: Resource<ProfileInputOptions> = Resource.Idle(),
    val inputsCoordinateY: Map<FormProfileInputKey, Float> = mapOf(),
    val firstErrorInputKey: FormProfileInputKey? = null,
    val submitResult: Resource<ProfileData> = Resource.Idle()
)

val formProfileStateDummy = FormProfileUiState(
    inputs = mapOf(
        FormProfileInputKey.MOTHER_NAME to InputTextData(
            inputName = FormProfileInputKey.MOTHER_NAME.toString(),
            value = "",
            validations = listOf(
                TextValidationType.Required,
            )
        ),
        FormProfileInputKey.SUB_DISTRICT to InputTextData(
            inputName = FormProfileInputKey.SUB_DISTRICT.toString(),
            validations = listOf(TextValidationType.Required),
            value = "",
            options = listOf(
                InputTextData.Option(
                    key = "desa",
                    value = "1",
                    label = "Bendo"
                ),
                InputTextData.Option(
                    key = "desa",
                    value = "2",
                    label = "Bendogerit"
                )
            )
        ),
        FormProfileInputKey.RT to InputTextData(
            inputName = FormProfileInputKey.RT.toString(),
            validations = listOf(TextValidationType.Required),
            value = ""
        ),
        FormProfileInputKey.PHONE_NUMBER to InputTextData(
            inputName = FormProfileInputKey.PHONE_NUMBER.toString(),
            validations = listOf(TextValidationType.Required, TextValidationType.MaxLength(13)),
            value = ""
        ),
        FormProfileInputKey.RW to InputTextData(
            inputName = FormProfileInputKey.RW.toString(),
            validations = listOf(TextValidationType.Required),
            value = ""
        ),
        FormProfileInputKey.BLOOD_TYPE to InputTextData(
            inputName = FormProfileInputKey.BLOOD_TYPE.toString(),
            validations = listOf(TextValidationType.Required),
            value = "",
            options = listOf(
                InputTextData.Option(
                    value = "8",
                    label = "A",
                    key = "GOLONGAN_DARAH"
                ),
                InputTextData.Option(
                    value = "9",
                    label = "A-",
                    key = "GOLONGAN_DARAH"
                )
            )
        ),
        FormProfileInputKey.FULL_NAME to InputTextData(
            inputName = FormProfileInputKey.FULL_NAME.toString(),
            validations = listOf(),
            value = ""
        ),
        FormProfileInputKey.RELIGION to InputTextData(
            inputName = FormProfileInputKey.RELIGION.toString(),
            validations = listOf(TextValidationType.Required),
            value = "",
            options = listOf(
                InputTextData.Option(
                    value = "1",
                    label = "Islam",
                    key = "agama"
                ),
                InputTextData.Option(
                    value = "2",
                    label = "Islam",
                    key = "agama"
                )
            )
        ),
        FormProfileInputKey.LONGITUDE to InputTextData(
            inputName = FormProfileInputKey.LONGITUDE.toString(),
            validations = listOf(),
            value = ""
        ),
        FormProfileInputKey.ID_NUMBER to InputTextData(
            validations = listOf(TextValidationType.Required),
            value = "",
            inputName = FormProfileInputKey.ID_NUMBER.toString()
        ),
        FormProfileInputKey.BIRTH_PLACE to InputTextData(
            inputName = FormProfileInputKey.BIRTH_PLACE.toString(),
            validations = listOf(TextValidationType.Required),
            value = "tempatLahir"
        ),
        FormProfileInputKey.FAMILY_RELATION to InputTextData(
            inputName = FormProfileInputKey.FAMILY_RELATION.toString(),
            validations = listOf(TextValidationType.Required),
            value = "",
            options = listOf(
                InputTextData.Option(
                    value = "118",
                    label = "ANAK",
                    key = "HUBUNGAN_KELUARGA"
                ),
                InputTextData.Option(
                    value = "119",
                    label = "CUCU",
                    key = "HUBUNGAN_KELUARGA"
                )
            )
        ),
        FormProfileInputKey.FAM_CARD_NUMBER to InputTextData(
            inputName = FormProfileInputKey.FAM_CARD_NUMBER.toString(),
            validations = listOf(),
            value = "noKk"
        ),
        FormProfileInputKey.GENDER to InputTextData(
            inputName = FormProfileInputKey.GENDER.toString(),
            validations = listOf(TextValidationType.Required),
            value = "",
            options = listOf(
                InputTextData.Option(
                    key = "JENIS_KELAMIN",
                    value = "21",
                    label = "Laki-Laki"
                ),
                InputTextData.Option(
                    key = "JENIS_KELAMIN",
                    value = "22",
                    label = "Perempuan"
                )
            )
        ),
        FormProfileInputKey.BIRTH_DATE to InputTextData(
            inputName = FormProfileInputKey.BIRTH_DATE.toString(),
            validations = listOf(),
            value = "09 September 2009"
        ),
        FormProfileInputKey.EDUCATION to InputTextData(
            inputName = FormProfileInputKey.EDUCATION.toString(),
            validations = listOf(TextValidationType.Required),
            value = "",
            options = listOf(
                InputTextData.Option(
                    value = "98",
                    label = "TIDAK/BELUM SEKOLAH",
                    key = "PENDIDIKAN"
                ),
                InputTextData.Option(
                    value = "99",
                    label = "SD SEKOLAH",
                    key = "PENDIDIKAN"
                )
            )
        ),
        FormProfileInputKey.LATITUDE to InputTextData(
            inputName = FormProfileInputKey.LATITUDE.toString(),
            validations = listOf(),
            value = "lintang"
        ),
        FormProfileInputKey.DISTRICT to InputTextData(
            inputName = FormProfileInputKey.DISTRICT.toString(),
            validations = listOf(TextValidationType.Required),
            value = "",
            options = listOf(
                InputTextData.Option(
                    key = "kecamatan",
                    value = "1",
                    label = "Kepanjenkidul"
                ),
                InputTextData.Option(
                    key = "desa",
                    value = "2",
                    label = "Sananwetan"
                )
            )
        ),
        FormProfileInputKey.ADDRESS to InputTextData(
            inputName = FormProfileInputKey.ADDRESS.toString(),
            validations = listOf(TextValidationType.Required),
            value = ""
        ),
        FormProfileInputKey.PROFESSION to InputTextData(
            inputName = FormProfileInputKey.PROFESSION.toString(),
            validations = listOf(TextValidationType.Required),
            value = "",
            options = listOf(
                InputTextData.Option(
                    value = "23",
                    label = "ANGGOTA DPRD KABUPATEN/KOTA",
                    key = "PEKERJAAN"
                ),
                InputTextData.Option(
                    value = "24",
                    label = "AKUNTAN",
                    key = "PEKERJAAN"
                )
            )
        ),
        FormProfileInputKey.FATHER_NAME to InputTextData(
            inputName = FormProfileInputKey.FATHER_NAME.toString(),
            validations = listOf(TextValidationType.Required),
            value = ""
        ),
        FormProfileInputKey.MARRIAGE_STATUS to InputTextData(
            inputName = FormProfileInputKey.MARRIAGE_STATUS.toString(),
            validations = listOf(TextValidationType.Required),
            value = "",
            options = listOf(
                InputTextData.Option(
                    value = "1",
                    label = "Kawin",
                    key = "status_kawin"
                ),
                InputTextData.Option(
                    value = "2",
                    label = "Kawin Lagi",
                    key = "status_kawin"
                )
            )
        )
    )
)
