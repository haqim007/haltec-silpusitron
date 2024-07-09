package com.haltec.silpusitron.user.profile.domain.usecase

import com.haltec.silpusitron.data.mechanism.Resource
import com.haltec.silpusitron.user.profile.domain.IProfileRepository
import com.haltec.silpusitron.user.profile.domain.model.ProfileData
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetProfileUseCase: KoinComponent {
    private val repository: IProfileRepository by inject<IProfileRepository>()

    operator fun invoke(): Flow<Resource<ProfileData>>{
        return repository.getProfile()
    }

}