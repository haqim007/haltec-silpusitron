package com.silpusitron.feature.requirementdocs.common.domain

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetReqDocsUseCase: KoinComponent {
    private val repository: IReqDocsRepository by inject()

    operator fun invoke(
        searchKeyword: String? = null,
        letterTypeId: Int? = null,
        level: String? = null
    ): Flow<PagingData<RequirementDoc>> {
        return repository.getData(searchKeyword, letterTypeId, level)
    }
}