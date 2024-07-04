package com.haltec.silpusitron.core.domain.model


data class InputTextData<ValidationType, ValueType>(
    val inputName: String,
    val message: String? = null,
    val validationError: ValidationType? = null,
    val value: ValueType,
    val validations: List<ValidationType>,
    val everChanged: Boolean = false,
    val options: List<Option>? = null,
){
    val isValid get() = message == null && validationError == null

    fun setValue(newValue: ValueType) = this.copy(
        value = newValue,
        everChanged = if (!everChanged) {
            true
        } else {
            this.everChanged
        }
    )

    data class Option(
        val key: String,
        val value: String,
        val label: String
    )
}

fun InputTextData<TextValidationType, String>.isRequired(): Boolean{
    return validations.firstOrNull { it is TextValidationType.Required } != null
}

fun InputTextData<TextValidationType, String>.getMaxLength(): Int{
    val validation = this.validations.firstOrNull {
        it is TextValidationType.MaxLength
    }
    return (validation as? TextValidationType.MaxLength)?.maxLength ?: 0
}


fun InputTextData<TextValidationType, String>.validate(): InputTextData<TextValidationType, String> {
    var foundError = false
    var whichValidationError: TextValidationType? = null
    validations.forEach { validation ->
        if (foundError) return@forEach
        when(validation){
            is TextValidationType.Required -> {
                if (value.isBlank()){
                    whichValidationError = validation
                    foundError = true
                }
            }
            is TextValidationType.Email -> {
                val emailComponents = value.split("@")
                if (emailComponents.size == 1 || emailComponents[1].split(".").size == 1) {
                    whichValidationError = validation
                    foundError = true
                }
            }

            is TextValidationType.ExactLength -> {
                if (value.length != validation.length){
                    whichValidationError = validation
                    foundError = true
                }
            }

            is TextValidationType.MaxLength -> {
                if (value.length > validation.maxLength){
                    whichValidationError = validation
                    foundError = true
                }
            }
            is TextValidationType.MinLength -> {
                if (value.length < validation.minLength){
                    whichValidationError = validation
                    foundError = true
                }
            }
            else -> { // consider as valid input
                whichValidationError = null
            }
        }
    }

    return if (!foundError){
        this.copy(
            validationError = null, message = null
        )
    }else{
        this.copy(validationError = whichValidationError)
    }
}
