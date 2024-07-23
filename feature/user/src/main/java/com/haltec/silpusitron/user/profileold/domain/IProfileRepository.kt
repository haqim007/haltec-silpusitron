package com.haltec.silpusitron.user.profileold.domain

import com.haltec.silpusitron.data.mechanism.Resource
import com.haltec.silpusitron.shared.form.domain.model.InputTextData
import com.haltec.silpusitron.shared.form.domain.model.TextValidationType
import com.haltec.silpusitron.shared.formprofile.domain.model.FormProfileInputKey
import com.haltec.silpusitron.shared.formprofile.domain.model.ProfileData
import kotlinx.coroutines.flow.Flow

interface IProfileRepository {
    fun submitProfile(
        data: ProfileData,
        input: Map<FormProfileInputKey, InputTextData<TextValidationType, String>>
    ): Flow<Resource<ProfileData>>
}