package com.haltec.silpusitron.core.domain.model

data class InputTextData<ValidationType, ValueType>(
    val inputName: String,
    var message: String? = null,
    var validationError: ValidationType? = null,
    var value: ValueType,
    var validations: List<ValidationType>,
    var everChanged: Boolean = false
){
    val isValid get() = message == null && validationError == null

    fun setValue(newValue: ValueType) = apply {
        value = newValue
        if (!everChanged) everChanged = true
    }
}


fun InputTextData<TextValidationType, String>.validate() {
    var foundError = false
    validations.forEach { validation ->
        if (foundError) return@forEach
        when(validation){
            is TextValidationType.Required -> {
                if (value.isBlank()){
                    validationError = validation
                    foundError = true
                }
            }
            is TextValidationType.Email -> {
                val emailComponents = value.split("@")
                if (emailComponents.size == 1 || emailComponents[1].split(".").size == 1) {
                    validationError = validation
                    foundError = true
                }
            }

            is TextValidationType.ExactLength -> {
                if (value.length != validation.length){
                    validationError = validation
                    foundError = true
                }
            }

            is TextValidationType.MaxLength -> {
                if (value.length > validation.maxLength){
                    validationError = validation
                    foundError = true
                }
            }
            is TextValidationType.MinLength -> {
                if (value.length < validation.minLength){
                    validationError = validation
                    foundError = true
                }
            }
            else -> { // consider as valid input
                validationError = null
                message = null
            }
        }
    }

    if (!foundError){
        validationError = null
        message = null
    }
}
