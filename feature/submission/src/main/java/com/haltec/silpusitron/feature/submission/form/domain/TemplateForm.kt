package com.haltec.silpusitron.feature.submission.form.domain

import com.haltec.silpusitron.shared.form.domain.model.FileAbsolutePath
import com.haltec.silpusitron.shared.form.domain.model.FileValidationType
import com.haltec.silpusitron.shared.form.domain.model.InputTextData
import com.haltec.silpusitron.shared.form.domain.model.TextValidationType

typealias VariableId = Int
typealias DocId = Int
data class TemplateForm(
    val requirementDocs: Map<DocId, InputTextData<FileValidationType, FileAbsolutePath>>,
    val forms: Map<VariableId, InputTextData<TextValidationType, String>>
)

val templateFormDummy = TemplateForm(
    requirementDocs = mapOf(
        1 to InputTextData(
            inputName = "1",
            value = null,
            validations = listOf(FileValidationType.Required),
            inputLabel = "KTP"
        ),
        2 to InputTextData(
            inputName = "2",
            value = null,
            validations = listOf(FileValidationType.Required),
            inputLabel = "KK"
        )
    ),
    forms = mapOf(
        1 to InputTextData(
            inputName = "1",
            value = "",
            validations = listOf(TextValidationType.Required),
            inputLabel = "KTP"
        ),
        2 to InputTextData(
            inputName = "2",
            value = "",
            validations = listOf(TextValidationType.Required),
            inputLabel = "KK"
        )
    )
)