package com.haltec.silpusitron.shared.formprofile.domain.usecase

import com.haltec.silpusitron.data.mechanism.Resource
import com.haltec.silpusitron.shared.form.domain.model.InputTextData
import com.haltec.silpusitron.shared.form.domain.model.TextValidationType
import com.haltec.silpusitron.shared.formprofile.domain.model.FormProfileInputKey
import com.haltec.silpusitron.shared.formprofile.domain.model.ProfileData
import com.haltec.silpusitron.shared.formprofile.domain.repository.IFormProfileRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SubmitProfileUseCase: KoinComponent {
    private val repository: IFormProfileRepository by inject()

    operator fun invoke(
        data: ProfileData,
        input: Map<FormProfileInputKey, InputTextData<TextValidationType, String>>
    ): Flow<Resource<ProfileData>>{
        return repository.submitProfile(data, input)
    }
}