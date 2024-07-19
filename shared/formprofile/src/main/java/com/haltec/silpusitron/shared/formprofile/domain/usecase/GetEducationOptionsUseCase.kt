package com.haltec.silpusitron.shared.formprofile.domain.usecase

import com.haltec.silpusitron.data.mechanism.Resource
import com.haltec.silpusitron.shared.form.domain.model.InputOptions
import com.haltec.silpusitron.shared.formprofile.data.remote.FormOptionPath
import com.haltec.silpusitron.shared.formprofile.domain.repository.IFormProfileRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetEducationOptionsUseCase: KoinComponent {
    private val repository: IFormProfileRepository by inject()

    operator fun invoke(): Flow<Resource<InputOptions>>{
        return repository.getOptions(FormOptionPath.EDUCATION)
    }

}