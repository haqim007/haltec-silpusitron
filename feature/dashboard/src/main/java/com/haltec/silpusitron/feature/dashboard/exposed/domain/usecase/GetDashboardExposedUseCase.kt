package com.haltec.silpusitron.feature.dashboard.exposed.domain.usecase

import com.haltec.silpusitron.data.mechanism.Resource
import com.haltec.silpusitron.feature.dashboard.common.domain.model.DashboardData
import com.haltec.silpusitron.feature.dashboard.exposed.domain.repository.IDashboardExposedRepository
import com.haltec.silpusitron.feature.dashboard.user.domain.repository.IDashboardUserRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


class GetDashboardExposedUseCase: KoinComponent{

    private val repository: IDashboardExposedRepository by inject<IDashboardExposedRepository>()
    operator fun invoke(
        districtId: String? = null,
        startDate: String? = null,
        endDate: String? = null
    ): Flow<Resource<List<DashboardData>>>{
        return repository.getData(
            districtId, startDate, endDate
        )
    }
}