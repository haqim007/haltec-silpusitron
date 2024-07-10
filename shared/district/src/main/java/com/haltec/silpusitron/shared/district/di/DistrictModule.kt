package com.haltec.silpusitron.shared.district.di

import com.haltec.silpusitron.shared.district.data.remote.DistrictRemotesDataSource
import com.haltec.silpusitron.shared.district.data.remote.DistrictService
import com.haltec.silpusitron.shared.district.data.repository.DistrictRepository
import com.haltec.silpusitron.shared.district.domain.repository.IDistrictRepository
import com.haltec.silpusitron.shared.district.domain.usecase.GetDistrictsUseCase
import org.koin.dsl.module

val districtModule = module {
    factory { DistrictService(getProperty("BASE_URL"), getProperty("API_VERSION")) }
    factory { DistrictRemotesDataSource(get()) }
    factory<IDistrictRepository> { DistrictRepository(get(), get()) }
    factory { GetDistrictsUseCase() }
}