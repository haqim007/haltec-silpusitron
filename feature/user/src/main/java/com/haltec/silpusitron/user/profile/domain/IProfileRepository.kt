package com.haltec.silpusitron.user.profile.domain

import com.haltec.silpusitron.shared.form.domain.model.InputTextData
import com.haltec.silpusitron.shared.form.domain.model.TextValidationType
import com.haltec.silpusitron.data.mechanism.Resource
import com.haltec.silpusitron.user.profile.data.remote.FormOptionPath
import com.haltec.silpusitron.user.profile.domain.model.FormProfileInputKey
import com.haltec.silpusitron.user.profile.domain.model.ProfileData
import com.haltec.silpusitron.shared.form.domain.model.InputOptions
import kotlinx.coroutines.flow.Flow

interface IProfileRepository {
    fun getProfile(): Flow<Resource<ProfileData>>
    fun getOptions(optionPath: FormOptionPath): Flow<Resource<com.haltec.silpusitron.shared.form.domain.model.InputOptions>>
    fun getSubDistricts(districtId: String?): Flow<Resource<com.haltec.silpusitron.shared.form.domain.model.InputOptions>>
    fun submitProfile(
        data: ProfileData,
        input: Map<FormProfileInputKey, com.haltec.silpusitron.shared.form.domain.model.InputTextData<com.haltec.silpusitron.shared.form.domain.model.TextValidationType, String>>
    ): Flow<Resource<ProfileData>>
}