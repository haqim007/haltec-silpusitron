package com.haltec.silpusitron.user.profile.domain.usecase

import com.haltec.silpusitron.data.mechanism.Resource
import com.haltec.silpusitron.user.profile.data.remote.FormOptionPath
import com.haltec.silpusitron.user.profile.domain.IProfileRepository
import com.haltec.silpusitron.shared.form.domain.model.InputOptions
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetMarriageStatusOptionsUseCase: KoinComponent {
    private val repository: IProfileRepository by inject<IProfileRepository>()

    operator fun invoke(): Flow<Resource<com.haltec.silpusitron.shared.form.domain.model.InputOptions>>{
        return repository.getOptions(FormOptionPath.MARRIAGE_STATUS)
    }

}