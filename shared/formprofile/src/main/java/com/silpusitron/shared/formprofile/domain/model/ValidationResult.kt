package com.silpusitron.shared.formprofile.domain.model

import com.silpusitron.shared.form.domain.model.InputTextData
import com.silpusitron.shared.form.domain.model.TextValidationType

data class ValidationResult(
    val input: Map<FormProfileInputKey, InputTextData<TextValidationType, String>>,
    val firstErrorInputKey: FormProfileInputKey?,
    val isAllValid: Boolean
)
