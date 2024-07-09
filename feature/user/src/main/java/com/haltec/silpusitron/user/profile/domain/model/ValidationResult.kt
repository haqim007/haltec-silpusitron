package com.haltec.silpusitron.user.profile.domain.model

import com.haltec.silpusitron.core.domain.model.InputTextData
import com.haltec.silpusitron.core.domain.model.TextValidationType

data class ValidationResult(
    val input: Map<FormProfileInputKey, InputTextData<TextValidationType, String>>,
    val firstErrorInputKey: FormProfileInputKey?,
    val isAllValid: Boolean
)
