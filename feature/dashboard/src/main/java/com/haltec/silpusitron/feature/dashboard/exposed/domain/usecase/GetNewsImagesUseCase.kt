package com.haltec.silpusitron.feature.dashboard.exposed.domain.usecase

import com.haltec.silpusitron.data.mechanism.Resource
import com.haltec.silpusitron.feature.dashboard.exposed.domain.model.NewsImage
import com.haltec.silpusitron.feature.dashboard.exposed.domain.repository.IDashboardExposedRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetNewsImagesUseCase: KoinComponent {
    private val repository: IDashboardExposedRepository by inject()

    operator fun invoke(): Flow<Resource<List<NewsImage>>>{
        return repository.getNewsimages()
    }
}