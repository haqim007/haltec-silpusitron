package com.silpusitron.feature.requirementdocs.submission.domain.usecase

import com.silpusitron.data.mechanism.Resource
import com.silpusitron.feature.requirementdocs.common.domain.IReqDocsRepository
import com.silpusitron.shared.form.domain.model.InputOptions
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetLetterLevelOptionsUseCase: KoinComponent {
    private val repository: IReqDocsRepository by inject()

    operator fun invoke(): Flow<Resource<InputOptions>>{
        return repository.getLetterLevelOptions()

    }
}