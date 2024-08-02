package com.silpusitron.feature.requirementdocs.common.domain

import androidx.paging.PagingData
import com.silpusitron.data.mechanism.Resource
import com.silpusitron.shared.form.domain.model.InputOptions
import kotlinx.coroutines.flow.Flow

internal interface IReqDocsRepository {
    fun getData(
        searchKeyword: String? = null,
        letterTypeId: Int? = null,
        level: String? = null
    ): Flow<PagingData<RequirementDoc>>

    fun getLetterLevelOptions(): Flow<Resource<InputOptions>>
}