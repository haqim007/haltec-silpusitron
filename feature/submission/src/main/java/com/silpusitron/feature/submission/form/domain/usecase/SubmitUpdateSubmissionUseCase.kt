package com.silpusitron.feature.submission.form.domain.usecase

import com.silpusitron.data.mechanism.Resource
import com.silpusitron.feature.submission.form.domain.DocId
import com.silpusitron.feature.submission.form.domain.ISubmissionDocRepository
import com.silpusitron.feature.submission.form.domain.VariableId
import com.silpusitron.shared.form.domain.model.FileAbsolutePath
import com.silpusitron.shared.form.domain.model.FileValidationType
import com.silpusitron.shared.form.domain.model.InputTextData
import com.silpusitron.shared.form.domain.model.TextValidationType
import com.silpusitron.shared.formprofile.domain.model.FormProfileInputKey
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SubmitUpdateSubmissionUseCase: KoinComponent {
    private val repository: ISubmissionDocRepository by inject()

    operator fun invoke(
        submissionId: Int,
        profile: Map<FormProfileInputKey, InputTextData<TextValidationType, String>>,
        forms: Map<VariableId, InputTextData<TextValidationType, String>>,
        docs: Map<DocId, InputTextData<FileValidationType, FileAbsolutePath>>
    ): Flow<Resource<String>>{
        return repository.submitUpdate(submissionId, profile, docs, forms)
    }
}