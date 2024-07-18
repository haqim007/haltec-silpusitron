package com.haltec.silpusitron.shared.formprofile.domain.model

import com.haltec.silpusitron.shared.form.domain.model.InputTextData
import com.haltec.silpusitron.shared.form.domain.model.TextValidationType

data class ValidationResult(
    val input: Map<FormProfileInputKey, InputTextData<TextValidationType, String>>,
    val firstErrorInputKey: FormProfileInputKey?,
    val isAllValid: Boolean
)
