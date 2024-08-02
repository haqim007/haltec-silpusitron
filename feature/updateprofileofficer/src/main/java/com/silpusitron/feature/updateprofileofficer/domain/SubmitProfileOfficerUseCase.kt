package com.silpusitron.feature.updateprofileofficer.domain

import com.silpusitron.data.mechanism.Resource
import com.silpusitron.shared.form.domain.model.InputTextData
import com.silpusitron.shared.form.domain.model.TextValidationType
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SubmitProfileOfficerUseCase: KoinComponent {
    private val repository: IUpdateProfileOfficerRepository by inject()

    operator fun invoke(
        data: ProfileOfficerData,
        input: Map<FormProfileOfficerInputKey, InputTextData<TextValidationType, String>>
    ): Flow<Resource<ProfileOfficerData>> {
        return repository.submitProfile(data, input)
    }
}