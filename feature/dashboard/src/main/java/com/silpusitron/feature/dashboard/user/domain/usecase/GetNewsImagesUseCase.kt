package com.silpusitron.feature.dashboard.user.domain.usecase

import com.silpusitron.data.mechanism.Resource
import com.silpusitron.feature.dashboard.common.domain.model.NewsImage
import com.silpusitron.feature.dashboard.user.domain.repository.IDashboardUserRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetNewsImagesUseCase: KoinComponent {
    private val repository: IDashboardUserRepository by inject()

    operator fun invoke(): Flow<Resource<List<NewsImage>>>{
        return repository.getNewsImages()
    }
}