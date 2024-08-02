package com.silpusitron.shared.lettertype.domain

import com.silpusitron.data.mechanism.Resource
import com.silpusitron.shared.form.domain.model.InputOptions
import kotlinx.coroutines.flow.Flow

internal interface ILetterTypesRepository {
    fun getLetterTypeOptions(): Flow<Resource<InputOptions>>
}