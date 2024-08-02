package com.silpusitron.shared.district.domain.usecase

import com.silpusitron.data.mechanism.Resource
import com.silpusitron.shared.district.domain.repository.IDistrictRepository
import com.silpusitron.shared.form.domain.model.InputOptions
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetDistrictsUseCase : KoinComponent {
    private val repository: IDistrictRepository by inject<IDistrictRepository>()

    operator fun invoke(): Flow<Resource<InputOptions>> {
        return repository.getDistricts()
    }
}