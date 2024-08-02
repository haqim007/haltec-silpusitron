package com.silpusitron.shared.lettertype.domain

import com.silpusitron.data.mechanism.Resource
import com.silpusitron.shared.form.domain.model.InputOptions
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetLetterTypeOptionsUseCase: KoinComponent {
    private val repository: ILetterTypesRepository by inject()

    operator fun invoke(): Flow<Resource<InputOptions>>{
        return repository.getLetterTypeOptions()
    }
}