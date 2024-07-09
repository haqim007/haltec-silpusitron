package com.haltec.silpusitron.user.profile.domain.usecase

import com.haltec.silpusitron.data.mechanism.Resource
import com.haltec.silpusitron.user.profile.domain.IProfileRepository
import com.haltec.silpusitron.user.profile.domain.model.District
import com.haltec.silpusitron.user.profile.domain.model.ProfileInputOptions
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetDistrictsUseCase : KoinComponent {
    private val repository: IProfileRepository by inject<IProfileRepository>()

    operator fun invoke(): Flow<Resource<ProfileInputOptions>> {
        return repository.getDistricts()
    }
}