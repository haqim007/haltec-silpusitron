package com.haltec.silpusitron.feature.requirementdocs.ui

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.haltec.silpusitron.core.ui.ui.BaseViewModel
import com.haltec.silpusitron.feature.requirementdocs.domain.GetReqDocsUseCase
import com.haltec.silpusitron.feature.requirementdocs.domain.RequirementDoc
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ReqDocViewModel(
    private val getReqDocsUseCase: GetReqDocsUseCase
): BaseViewModel<ReqDocUiState, ReqDocUiAction>() {
    override val _state = MutableStateFlow<ReqDocUiState>(ReqDocUiState())

    private val _pagingFlow = MutableStateFlow<PagingData<RequirementDoc>>(PagingData.empty())
    val pagingFlow: Flow<PagingData<RequirementDoc>> = _pagingFlow.asStateFlow()
    
    override fun doAction(action: ReqDocUiAction) {
        when(action){
            ReqDocUiAction.LoadData -> {
                viewModelScope.launch {
                    getReqDocsUseCase().collectLatest {
                        _pagingFlow.emit(it)
                    }
                }
            }
        }
    }


}

data class ReqDocUiState(
    val filter: String = ""
)

sealed class ReqDocUiAction{
    data object LoadData: ReqDocUiAction()
}