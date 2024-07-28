package com.haltec.silpusitron.shared.lettertype.domain

import com.haltec.silpusitron.data.mechanism.Resource
import com.haltec.silpusitron.shared.form.domain.model.InputOptions
import kotlinx.coroutines.flow.Flow

internal interface ILetterTypesRepository {
    fun getLetterTypeOptions(): Flow<Resource<InputOptions>>
}