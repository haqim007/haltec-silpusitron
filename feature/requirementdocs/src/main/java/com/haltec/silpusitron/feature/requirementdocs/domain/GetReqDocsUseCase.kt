package com.haltec.silpusitron.feature.requirementdocs.domain

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetReqDocsUseCase: KoinComponent {
    private val repository: IReqDocsRepository by inject()

    operator fun invoke(): Flow<PagingData<RequirementDoc>> {
        return repository.getData()
    }
}