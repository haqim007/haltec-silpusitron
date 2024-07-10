package com.haltec.silpusitron.shared.form.ui

import com.haltec.silpusitron.core.ui.ui.BaseViewModel
import com.haltec.silpusitron.shared.form.domain.model.InputTextData
import com.haltec.silpusitron.shared.form.domain.model.TextValidationType
import com.haltec.silpusitron.shared.form.domain.model.validate

abstract class BaseFormViewModel<UiState, UIAction>: BaseViewModel<UiState, UIAction>() {
    protected fun updateStateInputText(
        inputState: InputTextData<TextValidationType, String>,
        newValue: String
    ): InputTextData<TextValidationType, String> {

        val newInputState = inputState
            .setValue(newValue)
            .validate()

        return newInputState
    }
}