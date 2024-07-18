package com.haltec.silpusitron.feature.requirementdocs.submission.domain.usecase

import com.haltec.silpusitron.data.mechanism.Resource
import com.haltec.silpusitron.feature.requirementdocs.common.domain.IReqDocsRepository
import com.haltec.silpusitron.shared.form.domain.model.InputOptions
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetLetterTypeOptionsUseCase: KoinComponent {
    private val repository: IReqDocsRepository by inject()

    operator fun invoke(): Flow<Resource<InputOptions>>{
        return repository.getLetterTypeOptions()
    }
}