package com.silpusitron.feature.submission.form.domain.usecase

import com.silpusitron.data.mechanism.Resource
import com.silpusitron.feature.submission.form.domain.ISubmissionDocRepository
import com.silpusitron.feature.submission.form.domain.SubmissionForms
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetSubmissionDraftUseCase: KoinComponent {
    private val repository: ISubmissionDocRepository by inject()

    operator fun invoke(id: Int): Flow<Resource<SubmissionForms>>{
        return repository.getDraftSubmission(id)
    }
}