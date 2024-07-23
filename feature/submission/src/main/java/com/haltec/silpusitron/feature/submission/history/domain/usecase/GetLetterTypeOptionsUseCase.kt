package com.haltec.silpusitron.feature.submission.history.domain.usecase

import com.haltec.silpusitron.data.mechanism.Resource
import com.haltec.silpusitron.feature.submission.history.domain.ISubmissionHistoryRepository
import com.haltec.silpusitron.shared.form.domain.model.InputOptions
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

// TODO: Refactor by creating new shared module called lettertypeoptions to avoid redundancy
class GetLetterTypeOptionsUseCase: KoinComponent {
    private val repository: ISubmissionHistoryRepository by inject()

    operator fun invoke(): Flow<Resource<InputOptions>> {
        return repository.getLetterTypeOptions()
    }
}