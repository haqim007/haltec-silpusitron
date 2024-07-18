package com.haltec.silpusitron.feature.submission.domain

import com.haltec.silpusitron.data.mechanism.Resource
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetTemplateUseCase: KoinComponent {
    private val repository: ISubmissionDocRepository by inject()

    operator fun invoke(id: Int): Flow<Resource<TemplateForm>>{
        return repository.getTemplate(id)
    }
}