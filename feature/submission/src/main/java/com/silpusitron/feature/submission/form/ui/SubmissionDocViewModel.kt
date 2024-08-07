package com.silpusitron.feature.submission.form.ui

import android.content.Context
import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.silpusitron.data.mechanism.Resource
import com.silpusitron.feature.submission.form.domain.DocId
import com.silpusitron.feature.submission.form.domain.SubmissionForms
import com.silpusitron.feature.submission.form.domain.VariableId
import com.silpusitron.feature.submission.form.domain.submissionFormsDummy
import com.silpusitron.feature.submission.form.domain.usecase.GetSubmissionDraftUseCase
import com.silpusitron.feature.submission.form.domain.usecase.GetTemplateUseCase
import com.silpusitron.feature.submission.form.domain.usecase.SubmitSubmissionUseCase
import com.silpusitron.feature.submission.form.domain.usecase.SubmitUpdateSubmissionUseCase
import com.silpusitron.shared.form.domain.model.FileAbsolutePath
import com.silpusitron.shared.form.domain.model.FileValidationType
import com.silpusitron.shared.form.domain.model.InputTextData
import com.silpusitron.shared.form.domain.model.TextValidationType
import com.silpusitron.shared.form.domain.model.validate
import com.silpusitron.shared.form.domain.model.validateFile
import com.silpusitron.shared.form.ui.BaseFormViewModel
import com.silpusitron.shared.formprofile.domain.model.FormProfileInputKey
import com.silpusitron.shared.formprofile.domain.model.ProfileDataDummy
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SubmissionDocViewModel(
    private val getTemplateUseCase: GetTemplateUseCase,
    private val submitSubmissionUseCase: SubmitSubmissionUseCase,
    private val getSubmissionDraftUseCase: GetSubmissionDraftUseCase,
    private val submitUpdateSubmissionUseCase: SubmitUpdateSubmissionUseCase,
): BaseFormViewModel<SubmissionDocUiState, SubmissionDocUiAction>() {
    override val _state = MutableStateFlow(SubmissionDocUiState())

    private val maxStepperIndex = 2

    init {
        state.map { it.templateId }.launchCollectLatest { templateId ->
            templateId?.let {
                getTemplate()
            }
        }

        state.map { it.draftSubmissionId }.launchCollectLatest { submissionId ->
            submissionId?.let {
                getSubmissionDraft()
            }
        }
    }

    override fun doAction(action: SubmissionDocUiAction) {
        when(action){
            is SubmissionDocUiAction.SetTemplateId -> {
                _state.update { state -> state.copy(templateId = action.id) }
            }
            SubmissionDocUiAction.GetTemplate -> getTemplate()
            is SubmissionDocUiAction.SetState -> _state.update { action.state }
            SubmissionDocUiAction.BackToPrevStepper -> decreaseStepper()
            SubmissionDocUiAction.ToNextStepper -> increaseStepper()
            is SubmissionDocUiAction.SetFileInput -> onSelectFile(action)
            SubmissionDocUiAction.ValidateAllDocs -> validateAllDocs()
            is SubmissionDocUiAction.SetInput -> setInput(action.input, action.value)
            SubmissionDocUiAction.ValidateForms -> validateForms()
            SubmissionDocUiAction.Submit -> {
                if (state.value.templateId != null){
                    submitNew()
                }else{
                    submitUpdate()
                }
            }
            is SubmissionDocUiAction.SetProfileData -> _state.update { state ->
                state.copy(profile = action.input)
            }

            SubmissionDocUiAction.ResetSubmitState -> _state.update { state ->
                state.copy(submitResult = Resource.Idle())
            }

            is SubmissionDocUiAction.AgreeToTheTerm -> _state.update { state ->
                state.copy(hasAgree = action.agree)
            }

            is SubmissionDocUiAction.SetDraftSubmissionId -> {
                _state.update { state -> state.copy(draftSubmissionId = action.id) }
            }
        }
    }

    private fun setInput(key: VariableId, value: String){
        val inputState = state.value.forms[key]
        if (inputState != null){

            val newInput = updateStateInputText(
                inputState,
                value
            )

            val newInputs = state.value.forms.toMutableMap()
            newInputs[key] = newInput

            _state.update { state ->
                state.copy(
                    forms = newInputs.toMap(),
                )
            }
        }
    }

    private fun validateForms(): Boolean {
        var isAllValid = true

        val newInputs = state.value.forms.toMutableMap()
        newInputs.forEach {
            newInputs[it.key] = it.value.validate()

            if (isAllValid && newInputs[it.key]?.isValid == false) {
                isAllValid = false
            }

            _state.update { state ->
                state.copy(
                    forms = newInputs
                )
            }
        }
        _state.update { state -> state.copy(allFormSubmissionValid = isAllValid) }


        return isAllValid

    }

    private fun submitNew(){
        viewModelScope.launch {
            if (validateForms()){
                submitSubmissionUseCase(
                    state.value.templateId!!,
                    state.value.profile,
                    state.value.forms,
                    state.value.requirementDocs,
                ).collectLatest {
                    _state.update { state ->
                        state.copy(
                            submitResult = it
                        )
                    }
                }
            }
        }
    }

    private fun submitUpdate(){
        viewModelScope.launch {
            if (validateForms()){
                submitUpdateSubmissionUseCase(
                    state.value.draftSubmissionId!!,
                    state.value.profile,
                    state.value.forms,
                    state.value.requirementDocs,
                ).collectLatest {
                    _state.update { state ->
                        state.copy(
                            submitResult = it
                        )
                    }
                }
            }
        }
    }


    private fun validateAllDocs() {
        var isAllValid = true

        val newInputs = state.value.requirementDocs.toMutableMap()
        newInputs.forEach {
            newInputs[it.key] = it.value.validateFile()

            if (isAllValid && newInputs[it.key]?.isValid == false) {
                isAllValid = false
            }

            _state.update { state ->
                state.copy(
                    requirementDocs = newInputs
                )
            }
        }

        if (!isAllValid) return

        increaseStepper()
    }

    private fun decreaseStepper() {
        if (state.value.stepperIndex > 0) {
            _state.update { state -> state.copy(stepperIndex = state.stepperIndex - 1) }
        }
    }

    private fun increaseStepper() {
        if (state.value.stepperIndex < maxStepperIndex) {
            _state.update { state -> state.copy(stepperIndex = state.stepperIndex + 1) }
        }
    }

    private fun onSelectFile(action: SubmissionDocUiAction.SetFileInput) {
        val docInput = state.value.requirementDocs[action.inputKey]
        if (docInput != null) {

            val newDocInput = updateStateInputFile(
                action.context,
                docInput,
                action.value
            )

            val newInputs = state.value.requirementDocs.toMutableMap()
            newInputs[action.inputKey] = newDocInput

            _state.update { state ->
                state.copy(
                    requirementDocs = newInputs,
                )
            }
        }
    }

    private fun getTemplate(){
        state.value.templateId?.let {
            getTemplateUseCase(it).launchCollectLatest {
                _state.update { state ->
                    state.copy(
                        submissionForms = it,
                    )
                }
                if (it is Resource.Success){
                    _state.update { state ->
                        state.copy(
                            requirementDocs = it.data?.requirementDocs ?:  mapOf(),
                            forms = it.data?.forms ?:  mapOf()
                        )
                    }
                }
            }
        }
    }

    private fun getSubmissionDraft(){
        state.value.draftSubmissionId?.let {
            getSubmissionDraftUseCase(it).launchCollectLatest {
                _state.update { state ->
                    state.copy(
                        submissionForms = it,
                    )
                }
                if (it is Resource.Success){
                    _state.update { state ->
                        state.copy(
                            requirementDocs = it.data?.requirementDocs ?:  mapOf(),
                            forms = it.data?.forms ?:  mapOf()
                        )
                    }
                }
            }
        }
    }
}

internal val submissionDocUiStateDummy = SubmissionDocUiState(
    submissionForms = Resource.Success(submissionFormsDummy),
    requirementDocs = submissionFormsDummy.requirementDocs,
    forms = submissionFormsDummy.forms,
    profile = ProfileDataDummy.input
)

data class SubmissionDocUiState(
    val templateId: Int? = null,
    val draftSubmissionId: Int? = null,
    val submissionForms: Resource<SubmissionForms> = Resource.Idle(),
    val requirementDocs: Map<DocId, InputTextData<FileValidationType, FileAbsolutePath>> = mapOf(),
    val forms: Map<VariableId, InputTextData<TextValidationType, String>> = mapOf(),
    val profile: Map<FormProfileInputKey, InputTextData<TextValidationType, String>> = mapOf(),
    val stepperIndex: Int = 0,
    val hasAgree: Boolean = false,
    val allFormSubmissionValid: Boolean = false,
    val submitResult: Resource<String> = Resource.Idle()
)

sealed class SubmissionDocUiAction{
    data class SetTemplateId(val id: Int): SubmissionDocUiAction()
    data class SetDraftSubmissionId(val id: Int): SubmissionDocUiAction()
    data object GetTemplate: SubmissionDocUiAction()
    data object ToNextStepper: SubmissionDocUiAction()
    data object BackToPrevStepper: SubmissionDocUiAction()
    data class SetFileInput(
        val context: Context,
        val inputKey: DocId,
        val value: Uri?
    ): SubmissionDocUiAction()
    data object ValidateAllDocs: SubmissionDocUiAction()
    data class SetInput(val input: VariableId, val value: String): SubmissionDocUiAction()
    data object ValidateForms: SubmissionDocUiAction()
    data class SetProfileData(
        val input: Map<FormProfileInputKey, InputTextData<TextValidationType, String>>
    ): SubmissionDocUiAction()
    data object Submit: SubmissionDocUiAction()
    data object ResetSubmitState: SubmissionDocUiAction()
    data class AgreeToTheTerm(val agree: Boolean = true): SubmissionDocUiAction()

    /*For preview only*/
    data class SetState(val state: SubmissionDocUiState) : SubmissionDocUiAction()
}