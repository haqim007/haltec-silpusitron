package com.silpusitron.feature.requirementdocs.simple.ui

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.haltec.silpusitron.core.ui.ui.BaseViewModel
import com.silpusitron.feature.requirementdocs.common.domain.GetReqDocsUseCase
import com.silpusitron.feature.requirementdocs.common.domain.RequirementDoc
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SimpleReqDocViewModel(
    private val getReqDocsUseCase: GetReqDocsUseCase
): BaseViewModel<Nothing?, SimpleReqDocUiAction>() {
    override val _state = MutableStateFlow(null)

    private val _pagingFlow = MutableStateFlow<PagingData<RequirementDoc>>(PagingData.empty())
    val pagingFlow: Flow<PagingData<RequirementDoc>> = _pagingFlow
        .asStateFlow()
        .cachedIn(viewModelScope)
    
    override fun doAction(action: SimpleReqDocUiAction) {
        when(action){
            SimpleReqDocUiAction.LoadData -> {
                viewModelScope.launch {
                    getReqDocsUseCase().collectLatest {
                        _pagingFlow.emit(it)
                    }
                }
            }
        }
    }


}

sealed class SimpleReqDocUiAction{
    data object LoadData: SimpleReqDocUiAction()
}