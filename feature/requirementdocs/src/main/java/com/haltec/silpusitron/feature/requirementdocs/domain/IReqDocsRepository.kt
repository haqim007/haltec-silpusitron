package com.haltec.silpusitron.feature.requirementdocs.domain

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

interface IReqDocsRepository {
    fun getData(): Flow<PagingData<RequirementDoc>>
}