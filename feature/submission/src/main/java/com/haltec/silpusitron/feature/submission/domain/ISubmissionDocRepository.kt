package com.haltec.silpusitron.feature.submission.domain

import com.haltec.silpusitron.data.mechanism.Resource
import kotlinx.coroutines.flow.Flow

interface ISubmissionDocRepository {
    fun getTemplate(id: Int): Flow<Resource<TemplateForm>>
}