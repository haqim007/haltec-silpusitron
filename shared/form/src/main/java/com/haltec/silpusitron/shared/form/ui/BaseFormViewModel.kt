package com.haltec.silpusitron.shared.form.ui

import android.content.Context
import android.net.Uri
import com.haltec.silpusitron.common.util.FileHelper
import com.haltec.silpusitron.core.ui.ui.BaseViewModel
import com.haltec.silpusitron.shared.form.domain.model.FileAbsolutePath
import com.haltec.silpusitron.shared.form.domain.model.FileValidationType
import com.haltec.silpusitron.shared.form.domain.model.InputTextData
import com.haltec.silpusitron.shared.form.domain.model.TextValidationType
import com.haltec.silpusitron.shared.form.domain.model.validateFile
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

    protected fun updateStateInputFile(
        context: Context,
        inputState: InputTextData<FileValidationType, FileAbsolutePath>,
        newValue: Uri?,
    ): InputTextData<FileValidationType, FileAbsolutePath> {
        val filePath = newValue?.let { FileHelper.getPath(context, it) }
        val newInputState = inputState
            .setValue(filePath)
            .validateFile()

        return newInputState
    }
}