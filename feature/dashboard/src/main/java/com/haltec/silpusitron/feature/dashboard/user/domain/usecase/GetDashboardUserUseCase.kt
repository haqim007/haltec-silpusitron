package com.haltec.silpusitron.feature.dashboard.common.domain.usecase

import com.haltec.silpusitron.data.mechanism.Resource
import com.haltec.silpusitron.feature.dashboard.common.domain.model.DashboardData
import com.haltec.silpusitron.feature.dashboard.user.domain.repository.IDashboardUserRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


class GetDashboardUserUseCase: KoinComponent{

    private val repository: IDashboardUserRepository by inject<IDashboardUserRepository>()
    operator fun invoke(): Flow<Resource<List<DashboardData>>>{
        return repository.getData()
    }
}