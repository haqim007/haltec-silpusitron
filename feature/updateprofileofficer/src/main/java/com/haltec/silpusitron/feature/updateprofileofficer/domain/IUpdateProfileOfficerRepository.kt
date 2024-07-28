package com.haltec.silpusitron.feature.updateprofileofficer.domain

import com.haltec.silpusitron.data.mechanism.Resource
import com.haltec.silpusitron.shared.form.domain.model.InputTextData
import com.haltec.silpusitron.shared.form.domain.model.TextValidationType
import kotlinx.coroutines.flow.Flow

interface IUpdateProfileOfficerRepository {
    fun getProfile(): Flow<Resource<ProfileOfficerData>>
    fun submitProfile(
        data: ProfileOfficerData,
        input: Map<FormProfileOfficerInputKey, InputTextData<TextValidationType, String>>
    ): Flow<Resource<ProfileOfficerData>>
}