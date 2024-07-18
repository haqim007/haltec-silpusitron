package com.haltec.silpusitron.shared.formprofile.domain.usecase

import com.haltec.silpusitron.data.mechanism.Resource
import com.haltec.silpusitron.shared.form.domain.model.InputOptions
import com.haltec.silpusitron.shared.formprofile.domain.repository.IFormProfileRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetSubDistrictsUseCase : KoinComponent {
    private val repository: IFormProfileRepository by inject()

    operator fun invoke(districtId: String?): Flow<Resource<InputOptions>> {
        return repository.getSubDistricts(districtId)
    }
}