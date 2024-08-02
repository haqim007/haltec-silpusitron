package com.silpusitron.shared.pdfviewer.data

import com.silpusitron.common.util.DispatcherProvider
import com.silpusitron.data.mechanism.Resource
import com.silpusitron.shared.auth.mechanism.AuthorizedNetworkBoundResource
import com.silpusitron.shared.auth.preference.AuthPreference
import com.silpusitron.shared.pdfviewer.domain.IPDFViewerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import java.io.File

internal class PDFViewerRepository(
    private val remoteDataSource: PDFViewerRemoteDataSource,
    private val authPreference: AuthPreference,
    private val dispatcherProvider: DispatcherProvider
): IPDFViewerRepository {

    override fun getStreamedPDF(title: String, fileUrl: String): Flow<Resource<File>> {
        return object: AuthorizedNetworkBoundResource<File, File>(authPreference){

            override suspend fun requestFromRemote(): Result<File> {
                return remoteDataSource.getStreamedPDF(
                    getToken(), title, fileUrl
                )
            }

            override fun loadResult(responseData: File): Flow<File> {
                return flowOf(responseData)
            }

        }.asFlow()
            .flowOn(dispatcherProvider.io)
    }

}
