package com.silpusitron.feature.officertask.docapproval


import androidx.lifecycle.viewModelScope
import com.silpusitron.core.ui.ui.BaseViewModel
import com.silpusitron.data.mechanism.Resource
import com.silpusitron.feature.officertask.common.domain.RejectingUseCase
import com.silpusitron.feature.officertask.common.domain.SigningUseCase
import com.silpusitron.shared.pdfviewer.domain.GetPDFUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File

class DocPreviewApprovalViewModel(
    private val getPDFUseCase: GetPDFUseCase,
    private val signingUseCase: SigningUseCase,
    private val rejectingUseCase: RejectingUseCase
): BaseViewModel<DocPreviewApprovalUiState, DocPreviewApprovalUiAction>() {
    override val _state = MutableStateFlow(DocPreviewApprovalUiState())


    override fun doAction(action: DocPreviewApprovalUiAction) {
        when(action){
            is DocPreviewApprovalUiAction.GetPDF -> getPDF(action.title, action.fileUrl)
            DocPreviewApprovalUiAction.ResetDownloadFileResult -> _state.update { state ->
                state.copy(downloadFileResult = Resource.Idle())
            }
            is DocPreviewApprovalUiAction.Rejecting -> rejecting(action.id, action.reason)
            is DocPreviewApprovalUiAction.Signing -> signing(action.id, action.passphrase)

            is DocPreviewApprovalUiAction.SetDummyState -> _state.update { action.state }
            DocPreviewApprovalUiAction.ResetSigningResult -> _state.update { state ->
                state.copy(signingResult = Resource.Idle())
            }
        }
    }

    private fun rejecting(id: Int, reason: String){
        viewModelScope.launch {
            rejectingUseCase(
                ids = listOf(id),
                reason = reason
            ).collectLatest {
                _state.update { state ->
                    state.copy(signingResult = it)
                }
            }
        }
    }

    private fun signing(id: Int, passphrase: String){
        viewModelScope.launch {
            signingUseCase(
                ids = listOf(id),
                passphrase = passphrase
            ).collectLatest {
                _state.update { state ->
                    state.copy(signingResult = it)
                }
            }

        }
    }

    private fun getPDF(title: String, fileUrl: String){
        viewModelScope.launch {
            getPDFUseCase(title, fileUrl).collectLatest {
                _state.update { state ->
                    state.copy(
                        downloadFileResult = it
                    )
                }
            }
        }
    }


}


sealed class DocPreviewApprovalUiAction{
    data class GetPDF(val title: String, val fileUrl: String): DocPreviewApprovalUiAction()
    data object ResetDownloadFileResult: DocPreviewApprovalUiAction()
    data class Rejecting(val id: Int, val reason: String): DocPreviewApprovalUiAction()
    data class Signing(val id: Int, val passphrase: String): DocPreviewApprovalUiAction()
    data object ResetSigningResult: DocPreviewApprovalUiAction()


    data class SetDummyState(val state: DocPreviewApprovalUiState): DocPreviewApprovalUiAction()
}

data class DocPreviewApprovalUiState(
    val downloadFileResult: Resource<File> = Resource.Idle(),
    val signingResult: Resource<String> = Resource.Idle()
)