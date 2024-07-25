package com.haltec.silpusitron.feature.submission.history.domain.usecase

import com.haltec.silpusitron.data.mechanism.Resource
import com.haltec.silpusitron.feature.submission.history.domain.ISubmissionHistoryRepository
import com.haltec.silpusitron.feature.submission.history.domain.SubmissionHistory
import com.haltec.silpusitron.shared.form.domain.model.InputOptions
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.File

class GetPDFUseCase: KoinComponent {
    private val repository: ISubmissionHistoryRepository by inject()

    operator fun invoke(history: SubmissionHistory): Flow<Resource<File>> {
        return repository.getStreamedPDF(history)
    }
}