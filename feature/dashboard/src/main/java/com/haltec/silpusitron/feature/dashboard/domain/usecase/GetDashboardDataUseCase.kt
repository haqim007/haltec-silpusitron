package com.haltec.silpusitron.feature.dashboard.domain.usecase

import com.haltec.silpusitron.data.mechanism.Resource
import com.haltec.silpusitron.feature.dashboard.domain.model.DashboardData
import com.haltec.silpusitron.feature.dashboard.domain.repository.IDashboardRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


class GetDashboardDataUseCase: KoinComponent{

    private val repository: IDashboardRepository by inject<IDashboardRepository>()
    operator fun invoke(): Flow<Resource<DashboardData>>{
        return repository.getData()
    }
}