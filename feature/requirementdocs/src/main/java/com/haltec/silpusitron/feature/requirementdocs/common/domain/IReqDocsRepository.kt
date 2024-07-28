package com.haltec.silpusitron.feature.requirementdocs.common.domain

import androidx.paging.PagingData
import com.haltec.silpusitron.data.mechanism.Resource
import com.haltec.silpusitron.shared.form.domain.model.InputOptions
import kotlinx.coroutines.flow.Flow

internal interface IReqDocsRepository {
    fun getData(
        searchKeyword: String? = null,
        letterTypeId: Int? = null,
        level: String? = null
    ): Flow<PagingData<RequirementDoc>>

    fun getLetterLevelOptions(): Flow<Resource<InputOptions>>
}