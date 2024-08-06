package com.silpusitron.feature.submission.form.data

import com.silpusitron.feature.submission.form.data.remote.response.DraftSubmissionResponse
import com.silpusitron.feature.submission.form.data.remote.response.TemplateResponse
import com.silpusitron.feature.submission.form.domain.SubmissionForms
import com.silpusitron.shared.form.domain.model.FileAbsolutePath
import com.silpusitron.shared.form.domain.model.FileValidationType
import com.silpusitron.shared.form.domain.model.InputTextData
import com.silpusitron.shared.form.domain.model.TextValidationType

internal fun TemplateResponse.toModel(): SubmissionForms {
    val inputFiles: MutableMap<Int, InputTextData<FileValidationType, FileAbsolutePath>> = mutableMapOf()
    val inputForms: MutableMap<Int, InputTextData<TextValidationType, String>> = mutableMapOf()
    this.data.berkasPersyaratanId?.forEach { requirementDocsItem ->
        val validations : MutableList<FileValidationType> = mutableListOf()
        if (requirementDocsItem.required) validations.add(FileValidationType.Required)
        inputFiles[requirementDocsItem.id] = InputTextData(
            inputName = requirementDocsItem.id.toString(),
            validations = validations,
            value = null,
            inputLabel = requirementDocsItem.berkasPersyaratan
        )
    }
    this.data.variableId?.forEach { item ->
        if (!item.jsonMemberDefault){
            val validations : MutableList<TextValidationType> = mutableListOf()
            if (item.required) validations.add(TextValidationType.Required)
            inputForms[item.id] = InputTextData(
                validations = validations,
                inputName = item.id.toString(),
                value = "",
                inputLabel = item.variable
            )
        }
    }
    return SubmissionForms(inputFiles, inputForms)
}

internal fun DraftSubmissionResponse.toModel(): SubmissionForms {
    val inputFiles: MutableMap<Int, InputTextData<FileValidationType, FileAbsolutePath>> = mutableMapOf()
    val inputForms: MutableMap<Int, InputTextData<TextValidationType, String>> = mutableMapOf()
    this.data.berkasPersyaratan.forEach { requirementDocsItem ->
        val validations : MutableList<FileValidationType> = mutableListOf()
        if (requirementDocsItem.required) validations.add(FileValidationType.Required)
        inputFiles[requirementDocsItem.id.toInt()] = InputTextData(
            inputName = requirementDocsItem.id,
            validations = validations,
            value = requirementDocsItem.path,
            inputLabel = requirementDocsItem.label
        )
    }
    this.data.formulir.forEachIndexed { index, item ->
        if (!item.jsonMemberDefault){
            val validations : MutableList<TextValidationType> = mutableListOf()
            if (item.required) validations.add(TextValidationType.Required)
            inputForms[index] = InputTextData(
                validations = validations,
                inputName = item.key,
                value = item.value,
                inputLabel = item.label
            )
        }
    }
    return SubmissionForms(inputFiles, inputForms)
}