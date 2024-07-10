package com.haltec.silpusitron.shared.district.domain.repository

import com.haltec.silpusitron.data.mechanism.Resource
import com.haltec.silpusitron.shared.form.domain.model.InputOptions
import kotlinx.coroutines.flow.Flow

interface IDistrictRepository {
    fun getDistricts(): Flow<Resource<InputOptions>>
}