package com.silpusitron.feature.submissionhistory.common.domain.usecase

import com.silpusitron.data.mechanism.Resource
import com.silpusitron.feature.submissionhistory.common.domain.ISubmissionHistoryRepository
import com.silpusitron.shared.form.domain.model.InputOptions
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetLetterStatusOptionsUseCase: KoinComponent {
    private val repository: ISubmissionHistoryRepository by inject()

    operator fun invoke(): Flow<Resource<InputOptions>> {
        return repository.getLetterStatusOptions()
    }
}