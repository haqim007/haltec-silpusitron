package com.silpusitron.shared.formprofile.domain.usecase

import com.silpusitron.data.mechanism.Resource
import com.silpusitron.shared.formprofile.domain.model.ProfileData
import com.silpusitron.shared.formprofile.domain.repository.IFormProfileRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetProfileUseCase: KoinComponent {
    private val repository: IFormProfileRepository by inject<IFormProfileRepository>()

    operator fun invoke(): Flow<Resource<ProfileData>>{
        return repository.getProfile()
    }

}