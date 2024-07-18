package com.haltec.silpusitron.feature.submission.ui

import android.content.Context
import android.net.Uri
import com.haltec.silpusitron.data.mechanism.Resource
import com.haltec.silpusitron.feature.submission.domain.DocId
import com.haltec.silpusitron.feature.submission.domain.GetTemplateUseCase
import com.haltec.silpusitron.feature.submission.domain.TemplateForm
import com.haltec.silpusitron.feature.submission.domain.VariableId
import com.haltec.silpusitron.feature.submission.domain.templateFormDummy
import com.haltec.silpusitron.shared.form.domain.model.FileAbsolutePath
import com.haltec.silpusitron.shared.form.domain.model.FileValidationType
import com.haltec.silpusitron.shared.form.domain.model.InputTextData
import com.haltec.silpusitron.shared.form.domain.model.TextValidationType
import com.haltec.silpusitron.shared.form.domain.model.validate
import com.haltec.silpusitron.shared.form.domain.model.validateFile
import com.haltec.silpusitron.shared.form.ui.BaseFormViewModel
import com.haltec.silpusitron.shared.formprofile.domain.model.FormProfileInputKey
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class SubmissionDocViewModel(
    private val getTemplateUseCase: GetTemplateUseCase
): BaseFormViewModel<SubmissionDocUiState, SubmissionDocUiAction>() {
    override val _state = MutableStateFlow(SubmissionDocUiState())

    private val maxStepperIndex = 2

    init {
        state.map { it.templateId }.launchCollectLatest { templateId ->
            templateId?.let {
                getTemplate()
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

    private fun validateForms() {
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

        if (!isAllValid) return


    }

    private fun submit(){

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
                        template = it,
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
    template = Resource.Success(templateFormDummy),
    requirementDocs = templateFormDummy.requirementDocs,
    forms = templateFormDummy.forms
)

data class SubmissionDocUiState(
    val templateId: Int? = null,
    val template: Resource<TemplateForm> = Resource.Idle(),
    val requirementDocs: Map<DocId, InputTextData<FileValidationType, FileAbsolutePath>> = mapOf(),
    val forms: Map<VariableId, InputTextData<TextValidationType, String>> = mapOf(),
    val stepperIndex: Int = 0
)

sealed class SubmissionDocUiAction{
    data class SetTemplateId(val id: Int): SubmissionDocUiAction()
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

    /*For preview only*/
    data class SetState(val state: SubmissionDocUiState) : SubmissionDocUiAction()
}