package com.silpusitron.feature.updateprofileofficer.domain

import com.silpusitron.data.mechanism.Resource
import com.silpusitron.shared.form.domain.model.InputTextData
import com.silpusitron.shared.form.domain.model.TextValidationType
import kotlinx.coroutines.flow.Flow

interface IUpdateProfileOfficerRepository {
    fun getProfile(): Flow<Resource<ProfileOfficerData>>
    fun submitProfile(
        data: ProfileOfficerData,
        input: Map<FormProfileOfficerInputKey, InputTextData<TextValidationType, String>>
    ): Flow<Resource<ProfileOfficerData>>
}