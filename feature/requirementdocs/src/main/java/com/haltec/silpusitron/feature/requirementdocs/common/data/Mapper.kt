package com.haltec.silpusitron.feature.requirementdocs.common.data

import com.haltec.silpusitron.feature.requirementdocs.common.data.remote.response.RequirementDocsResponse
import com.haltec.silpusitron.feature.requirementdocs.common.domain.RequirementDoc
import com.haltec.silpusitron.shared.form.domain.model.FileValidationType
import com.haltec.silpusitron.shared.form.domain.model.InputTextData
import com.haltec.silpusitron.shared.form.domain.model.TextValidationType
import java.io.File

internal fun RequirementDocsResponse.toModel() = this.data.data.map {
    val requirementDocNames: MutableList<String> = mutableListOf()
    val inputFiles: MutableMap<Int, InputTextData<FileValidationType, File>> = mutableMapOf()
    val inputForms: MutableMap<Int, InputTextData<TextValidationType, String>> = mutableMapOf()

    it.requirementDocs?.forEach { requirementDocsItem ->
        requirementDocNames.add(requirementDocsItem.berkasPersyaratan)
        val validations : MutableList<FileValidationType> = mutableListOf()
        if (requirementDocsItem.required) validations.add(FileValidationType.Required)
        inputFiles[requirementDocsItem.id] = InputTextData(
            inputName = requirementDocsItem.id.toString(),
            validations = validations,
            value = null,
            inputLabel = requirementDocsItem.berkasPersyaratan
        )
    }
    it.variable?.forEach { item ->
        if (!item.hide){
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
    RequirementDoc(
        requirementDocs = requirementDocNames,
        letterType = it.jenisSurat,
        id = it.id,
        isTt1 = it.isTt1,
        isTt2 = it.isTt2,
        letterLevel = it.levelSurat,
        letterTypeId = it.jenisSuratId,
        level = it.level,
        title = it.title,
        inputFiles = inputFiles,
        inputForms = inputForms
    )
}