package com.haltec.silpusitron.user.profile.domain.usecase

import com.haltec.silpusitron.core.domain.model.InputTextData
import com.haltec.silpusitron.core.domain.model.TextValidationType
import com.haltec.silpusitron.core.domain.model.validate
import com.haltec.silpusitron.user.profile.domain.model.FormProfileInputKey
import com.haltec.silpusitron.user.profile.domain.model.ValidationResult
import org.koin.core.component.KoinComponent

class ValidateAllInputUseCase {

    operator fun invoke(
        inputs: Map<FormProfileInputKey, InputTextData<TextValidationType, String>>
    ): ValidationResult{
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