package com.haltec.silpusitron.feature.updateprofileofficer.domain

import com.haltec.silpusitron.data.mechanism.Resource
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetProfileOfficerUseCase: KoinComponent {
    private val repository: IUpdateProfileOfficerRepository by inject()

    operator fun invoke(): Flow<Resource<ProfileOfficerData>> {
        return repository.getProfile()
    }

}