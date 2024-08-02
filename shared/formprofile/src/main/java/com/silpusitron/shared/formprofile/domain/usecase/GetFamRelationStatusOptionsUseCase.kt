package com.silpusitron.shared.formprofile.domain.usecase

import com.silpusitron.data.mechanism.Resource
import com.silpusitron.shared.form.domain.model.InputOptions
import com.silpusitron.shared.formprofile.data.remote.FormOptionPath
import com.silpusitron.shared.formprofile.domain.repository.IFormProfileRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetFamRelationStatusOptionsUseCase: KoinComponent {
    private val repository: IFormProfileRepository by inject()

    operator fun invoke(): Flow<Resource<InputOptions>>{
        return repository.getOptions(FormOptionPath.FAM_RELATION_STATUS)
    }

}