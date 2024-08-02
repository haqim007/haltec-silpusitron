package com.silpusitron.shared.formprofile.domain.usecase

import com.silpusitron.shared.form.domain.model.InputTextData
import com.silpusitron.shared.form.domain.model.TextValidationType
import com.silpusitron.shared.form.domain.model.validate
import com.silpusitron.shared.formprofile.domain.model.FormProfileInputKey
import com.silpusitron.shared.formprofile.domain.model.ValidationResult


class ValidateAllInputUseCase {

    operator fun invoke(
        inputs: Map<FormProfileInputKey, InputTextData<TextValidationType, String>>
    ): ValidationResult {
        val validatedInput = inputs.toMutableMap()
        inputs.forEach {
            validatedInput[it.key] = it.value.validate()
        }
        val isAllValid: Boolean = validatedInput.entries.find { !it.value.isValid } == null

        return ValidationResult(
            inputs,
            validatedInput.entries.find { !it.value.isValid }?.key,
            isAllValid
        )

    }
}