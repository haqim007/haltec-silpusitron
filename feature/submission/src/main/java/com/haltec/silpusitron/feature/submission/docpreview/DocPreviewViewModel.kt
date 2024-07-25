package com.haltec.silpusitron.feature.submission.docpreview


import androidx.lifecycle.viewModelScope
import com.haltec.silpusitron.core.ui.ui.BaseViewModel
import com.haltec.silpusitron.data.mechanism.Resource
import com.haltec.silpusitron.feature.submission.history.domain.SubmissionHistory
import com.haltec.silpusitron.feature.submission.history.domain.usecase.GetPDFUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File

class DocPreviewViewModel(
    private val getPDFUseCase: GetPDFUseCase
): BaseViewModel<DocPreviewUiState, DocPreviewUiAction>() {
    override val _state = MutableStateFlow(DocPreviewUiState())


    override fun doAction(action: DocPreviewUiAction) {
        when(action){
            is DocPreviewUiAction.GetPDF -> getPDF(action.history)
            DocPreviewUiAction.ResetDownloadFileResult -> _state.update { state ->
                state.copy(downloadFileResult = Resource.Idle())
            }
        }
    }

    private fun getPDF(history: SubmissionHistory){
        viewModelScope.launch {
            getPDFUseCase(history).collectLatest {
                _state.update { state ->
                    state.copy(
                        downloadFileResult = it
                    )
                }
            }
        }
    }


}


sealed class DocPreviewUiAction{
    data class GetPDF(val history: SubmissionHistory): DocPreviewUiAction()
    data object ResetDownloadFileResult: DocPreviewUiAction()
}

data class DocPreviewUiState(
    val downloadFileResult: Resource<File> = Resource.Idle()
)