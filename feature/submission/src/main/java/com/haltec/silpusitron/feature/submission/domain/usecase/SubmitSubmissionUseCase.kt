package com.haltec.silpusitron.feature.submission.domain.usecase

import com.haltec.silpusitron.data.mechanism.Resource
import com.haltec.silpusitron.feature.submission.domain.DocId
import com.haltec.silpusitron.feature.submission.domain.ISubmissionDocRepository
import com.haltec.silpusitron.feature.submission.domain.VariableId
import com.haltec.silpusitron.shared.form.domain.model.FileAbsolutePath
import com.haltec.silpusitron.shared.form.domain.model.FileValidationType
import com.haltec.silpusitron.shared.form.domain.model.InputTextData
import com.haltec.silpusitron.shared.form.domain.model.TextValidationType
import com.haltec.silpusitron.shared.formprofile.domain.model.FormProfileInputKey
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SubmitSubmissionUseCase: KoinComponent {
    private val repository: ISubmissionDocRepository by inject()

    operator fun invoke(
        templateId: Int,
        profile: Map<FormProfileInputKey, InputTextData<TextValidationType, String>>,
        forms: Map<VariableId, InputTextData<TextValidationType, String>>,
        docs: Map<DocId, InputTextData<FileValidationType, FileAbsolutePath>>
    ): Flow<Resource<String>>{
        return repository.submit(templateId, profile, docs, forms)
    }
}