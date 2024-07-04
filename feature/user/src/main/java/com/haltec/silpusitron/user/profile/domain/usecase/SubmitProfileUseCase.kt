package com.haltec.silpusitron.user.profile.domain.usecase

import com.haltec.silpusitron.core.domain.model.InputTextData
import com.haltec.silpusitron.core.domain.model.TextValidationType
import com.haltec.silpusitron.data.mechanism.Resource
import com.haltec.silpusitron.user.profile.domain.IProfileRepository
import com.haltec.silpusitron.user.profile.domain.model.FormProfileInputKey
import com.haltec.silpusitron.user.profile.domain.model.ProfileData
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SubmitProfileUseCase: KoinComponent {
    private val repository: IProfileRepository by inject()

    operator fun invoke(
        data: ProfileData,
        input: Map<FormProfileInputKey, InputTextData<TextValidationType, String>>
    ): Flow<Resource<ProfileData>>{
        return repository.submitProfile(data, input)
    }
}