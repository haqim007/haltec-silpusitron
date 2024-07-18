package com.haltec.silpusitron.shared.district.data.remote

import com.haltec.silpusitron.data.mechanism.getResult

class DistrictRemotesDataSource(
    private val service: DistrictService
) {

    suspend fun getDistricts() = getResult {
        service.getDistricts()
    }
}