package com.haltec.silpusitron.shared.formprofile.domain.repository

import com.haltec.silpusitron.data.mechanism.Resource
import com.haltec.silpusitron.shared.form.domain.model.InputOptions
import com.haltec.silpusitron.shared.formprofile.data.remote.FormOptionPath
import com.haltec.silpusitron.shared.formprofile.domain.model.ProfileData
import kotlinx.coroutines.flow.Flow

interface IFormProfileRepository {
    fun getProfile(): Flow<Resource<ProfileData>>
    fun getOptions(optionPath: FormOptionPath): Flow<Resource<InputOptions>>
    fun getSubDistricts(districtId: String?): Flow<Resource<InputOptions>>
}