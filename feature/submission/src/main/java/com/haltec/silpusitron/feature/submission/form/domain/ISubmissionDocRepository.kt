package com.haltec.silpusitron.feature.submission.form.domain

import com.haltec.silpusitron.data.mechanism.Resource
import com.haltec.silpusitron.shared.form.domain.model.FileAbsolutePath
import com.haltec.silpusitron.shared.form.domain.model.FileValidationType
import com.haltec.silpusitron.shared.form.domain.model.InputTextData
import com.haltec.silpusitron.shared.form.domain.model.TextValidationType
import com.haltec.silpusitron.shared.formprofile.domain.model.FormProfileInputKey
import kotlinx.coroutines.flow.Flow

interface ISubmissionDocRepository {
    fun getTemplate(id: Int): Flow<Resource<TemplateForm>>
    fun submit(
        templateId: Int,
        profile: Map<FormProfileInputKey, InputTextData<TextValidationType, String>>,
        docs: Map<DocId, InputTextData<FileValidationType, FileAbsolutePath>>,
        forms: Map<VariableId, InputTextData<TextValidationType, String>>
    ): Flow<Resource<String>>
}