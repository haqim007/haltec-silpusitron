package com.silpusitron.shared.form.domain.model

import androidx.core.net.toUri
import com.silpusitron.common.util.FileHelper
import java.io.File


typealias FileAbsolutePath = String

/**
 * Input text data
 *
 * @param ValidationType
 * @param ValueType
 * @property inputName Unique input name
 * @property message error message if there any
 * @property validationError validation error type if there any
 * @property value
 * @property validations collections of validation
 * @property everChanged to flag that this input has ever been changed or not
 * @property inputLabel
 * @property options collection of options for this input. Also indicate this input as dropdown
 * @property enabled
 * @property prefix
 * @constructor Create empty Input text data
 */
data class InputTextData<ValidationType, ValueType>(
    val inputName: String,
    val message: String? = null,
    val validationError: ValidationType? = null,
    val value: ValueType?,
    val validations: List<ValidationType>,
    val everChanged: Boolean = false,
    val inputLabel: String = "",
    val options: List<Option>? = null,
    val enabled: Boolean = true,
    val prefix: String? = null
){
    val isValid get() = message == null && validationError == null

    fun setValue(newValue: ValueType?) = this.copy(
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

fun InputTextData<FileValidationType, FileAbsolutePath>.isRequiredFile(): Boolean{
    return validations.firstOrNull { it is FileValidationType.Required } != null
}

fun InputTextData<TextValidationType, String>.getMaxLength(): Int{
    val maxLength = this.validations.firstOrNull {
        it is TextValidationType.MaxLength
    }
    if (maxLength != null){
        return (maxLength as? TextValidationType.MaxLength)?.maxLength ?:  0
    }

    val exactLength = this.validations.firstOrNull { it is TextValidationType.ExactLength }
    if (exactLength != null){
        return (exactLength as? TextValidationType.ExactLength)?.length ?:  0
    }
    return 0
}

fun InputTextData<FileValidationType, FileAbsolutePath>.getFilename() =
    this.value?.let { FileHelper.getFileNameFromAbsolutePath(it) } ?: ""

fun InputTextData<FileValidationType, FileAbsolutePath>.validateFile():
        InputTextData<FileValidationType, FileAbsolutePath>
{
    var foundError = false
    var whichValidationError: FileValidationType? = null
    validations.forEach { validation ->
        if (foundError) return@forEach

        val fileSize =
            this.value?.let {
                File(it).toUri()
            }?.let {
                FileHelper.getFileSizeInMB(it)
            } ?: 0.0
        when(validation){
            FileValidationType.Required -> {
                if (value.isNullOrBlank()){
                    whichValidationError = validation
                    foundError = true
                }
                else{
                    if (fileSize <= 0){
                        whichValidationError = validation
                        foundError = true
                    }
                }
            }
            is FileValidationType.MaxSize -> {
                if (fileSize > validation.limitInMB){
                    whichValidationError = validation
                    foundError = true
                }
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

fun InputTextData<TextValidationType, String>.valueOrEmpty() = this.value ?: ""
fun InputTextData<TextValidationType, String>.validate(): InputTextData<TextValidationType, String> {
    var foundError = false
    var whichValidationError: TextValidationType? = null
    validations.forEach { validation ->
        if (foundError) return@forEach
        when(validation){
            is TextValidationType.Required -> {
                if (value.isNullOrBlank()){
                    whichValidationError = validation
                    foundError = true
                }
            }
            is TextValidationType.Email -> {
                val emailComponents = value?.split("@")
                if (emailComponents?.size == 1 || emailComponents?.get(1)?.split(".")?.size == 1) {
                    whichValidationError = validation
                    foundError = true
                }
            }

            is TextValidationType.ExactLength -> {
                if (value?.length != validation.length){
                    whichValidationError = validation
                    foundError = true
                }
            }

            is TextValidationType.MaxLength -> {
                if ((value?.length ?: 0) > validation.maxLength){
                    whichValidationError = validation
                    foundError = true
                }
            }
            is TextValidationType.MinLength -> {
                if ((value?.length ?: 0) < validation.minLength){
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
