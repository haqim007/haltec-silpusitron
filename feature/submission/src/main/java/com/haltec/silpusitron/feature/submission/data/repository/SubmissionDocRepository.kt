package com.haltec.silpusitron.feature.submission.data.repository

import com.haltec.silpusitron.common.util.DispatcherProvider
import com.haltec.silpusitron.data.mechanism.Resource
import com.haltec.silpusitron.feature.submission.data.remote.SubmissionDocRemoteDatasource
import com.haltec.silpusitron.feature.submission.data.remote.response.TemplateResponse
import com.haltec.silpusitron.feature.submission.data.toModel
import com.haltec.silpusitron.feature.submission.domain.ISubmissionDocRepository
import com.haltec.silpusitron.feature.submission.domain.TemplateForm
import com.haltec.silpusitron.shared.auth.mechanism.AuthorizedNetworkBoundResource
import com.haltec.silpusitron.shared.auth.preference.AuthPreference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn

internal class SubmissionDocRepository(
    private val remoteDatasource: SubmissionDocRemoteDatasource,
    private val preferences: AuthPreference,
    private val dispatcher: DispatcherProvider
): ISubmissionDocRepository {
    override fun getTemplate(id: Int): Flow<Resource<TemplateForm>> {
        return object : AuthorizedNetworkBoundResource<TemplateForm, TemplateResponse>(preferences) {
            override suspend fun getToken(): String {
                return preferences.getToken().first()
            }

            override suspend fun requestFromRemote(): Result<TemplateResponse> {
                return remoteDatasource.getTemplate(id, getToken())
            }

            override fun loadResult(responseData: TemplateResponse): Flow<TemplateForm> {
                return flowOf(responseData.toModel())
            }

        }
            .asFlow()
            .flowOn(dispatcher.io)
    }
}