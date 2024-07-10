package com.haltec.silpusitron.user.profile.domain.model

import com.haltec.silpusitron.shared.form.domain.model.InputTextData
import com.haltec.silpusitron.shared.form.domain.model.TextValidationType

data class ValidationResult(
    val input: Map<FormProfileInputKey, com.haltec.silpusitron.shared.form.domain.model.InputTextData<com.haltec.silpusitron.shared.form.domain.model.TextValidationType, String>>,
    val firstErrorInputKey: FormProfileInputKey?,
    val isAllValid: Boolean
)
