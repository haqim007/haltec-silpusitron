package com.haltec.silpusitron.user.profile.domain

import com.haltec.silpusitron.core.domain.model.InputTextData
import com.haltec.silpusitron.core.domain.model.TextValidationType
import com.haltec.silpusitron.data.mechanism.Resource
import com.haltec.silpusitron.user.profile.data.remote.FormOptionPath
import com.haltec.silpusitron.user.profile.domain.model.FormProfileInputKey
import com.haltec.silpusitron.user.profile.domain.model.ProfileData
import com.haltec.silpusitron.user.profile.domain.model.ProfileInputOptions
import kotlinx.coroutines.flow.Flow

interface IProfileRepository {
    fun getProfile(): Flow<Resource<ProfileData>>
    fun getOptions(optionPath: FormOptionPath): Flow<Resource<ProfileInputOptions>>
    fun getDistricts(): Flow<Resource<ProfileInputOptions>>
    fun getSubDistricts(districtId: String?): Flow<Resource<ProfileInputOptions>>
    fun submitProfile(
        data: ProfileData,
        input: Map<FormProfileInputKey, InputTextData<TextValidationType, String>>
    ): Flow<Resource<ProfileData>>
}