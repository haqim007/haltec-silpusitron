package com.silpusitron.shared.district.domain.repository

import com.silpusitron.data.mechanism.Resource
import com.silpusitron.shared.form.domain.model.InputOptions
import kotlinx.coroutines.flow.Flow

interface IDistrictRepository {
    fun getDistricts(): Flow<Resource<InputOptions>>
}