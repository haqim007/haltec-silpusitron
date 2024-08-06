package com.silpusitron.feature.submission.form.domain

import com.silpusitron.data.mechanism.Resource
import com.silpusitron.shared.form.domain.model.FileAbsolutePath
import com.silpusitron.shared.form.domain.model.FileValidationType
import com.silpusitron.shared.form.domain.model.InputTextData
import com.silpusitron.shared.form.domain.model.TextValidationType
import com.silpusitron.shared.formprofile.domain.model.FormProfileInputKey
import kotlinx.coroutines.flow.Flow

interface ISubmissionDocRepository {
    fun getTemplate(id: Int): Flow<Resource<SubmissionForms>>
    fun submit(
        templateId: Int,
        profile: Map<FormProfileInputKey, InputTextData<TextValidationType, String>>,
        docs: Map<DocId, InputTextData<FileValidationType, FileAbsolutePath>>,
        forms: Map<VariableId, InputTextData<TextValidationType, String>>
    ): Flow<Resource<String>>
    fun submitUpdate(
        submissionId: Int,
        profile: Map<FormProfileInputKey, InputTextData<TextValidationType, String>>,
        docs: Map<DocId, InputTextData<FileValidationType, FileAbsolutePath>>,
        forms: Map<VariableId, InputTextData<TextValidationType, String>>
    ): Flow<Resource<String>>

    fun getDraftSubmission(submissionId: Int): Flow<Resource<SubmissionForms>>
}