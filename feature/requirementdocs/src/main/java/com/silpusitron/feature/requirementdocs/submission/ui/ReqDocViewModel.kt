package com.silpusitron.feature.requirementdocs.submission.ui

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.silpusitron.core.ui.ui.BaseViewModel
import com.silpusitron.data.mechanism.Resource
import com.silpusitron.feature.requirementdocs.common.domain.GetReqDocsUseCase
import com.silpusitron.feature.requirementdocs.common.domain.RequirementDoc
import com.silpusitron.feature.requirementdocs.submission.domain.usecase.GetLetterLevelOptionsUseCase
import com.silpusitron.shared.form.domain.model.InputOptions
import com.silpusitron.shared.lettertype.domain.GetLetterTypeOptionsUseCase
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class ReqDocViewModel(
    private val getReqDocsUseCase: GetReqDocsUseCase,
    private val getLetterTypeOptionsUseCase: GetLetterTypeOptionsUseCase,
    private val getLetterLevelOptionsUseCase: GetLetterLevelOptionsUseCase
) : BaseViewModel<ReqDocUiState, ReqDocUiAction>() {
    override val _state = MutableStateFlow(ReqDocUiState())

    private val supervisorJob = SupervisorJob()

    private val _pagingFlow = MutableStateFlow<PagingData<RequirementDoc>>(PagingData.empty())
    val pagingFlow: Flow<PagingData<RequirementDoc>> = _pagingFlow
        .asStateFlow()
        .cachedIn(viewModelScope)

    init {
        viewModelScope.launch(supervisorJob) {
            state.map { it.searchKeyword }.distinctUntilChanged()
                .debounce(1500)
                .collectLatest {
                    loadData()
                }
        }
    }

    override fun doAction(action: ReqDocUiAction) {
        when(action){
            ReqDocUiAction.LoadData -> handleLoadData()
            is ReqDocUiAction.Search -> onSearch(action.keyword)
            is ReqDocUiAction.Filter -> onFilter()
            ReqDocUiAction.LoadLetterLevel -> loadLetterLevel()
            ReqDocUiAction.LoadLetterType -> loadLetterType()
            is ReqDocUiAction.SetFilterLetterLevel -> {
                _state.update { state ->
                    state.copy(filterLetterLevel = action.value)
                }
            }
            is ReqDocUiAction.SetFilterLetterType -> {
                _state.update { state -> state.copy(filterLetterTypeId = action.value?.toInt()) }
            }

            ReqDocUiAction.ResetFilter -> {
                _state.update {state ->
                    state.copy(
                        filterLetterTypeId = null,
                        filterLetterLevel = null,
                        filterActive = 0
                    )
                }
                handleLoadData()
            }
        }
    }

    private fun onFilter() {
        with(state.value) {
            if (filterLetterLevel != null && filterLetterTypeId != null) {
                _state.update { state -> state.copy(filterActive = 2) }
            } else if ((filterLetterLevel == null && filterLetterTypeId != null) ||
                (filterLetterLevel != null && filterLetterTypeId == null)
            ) {
                _state.update { state -> state.copy(filterActive = 1) }
            } else {
                _state.update { state -> state.copy(filterActive = 0) }
            }

        }
        handleLoadData()
    }

    private fun loadLetterLevel(){
        viewModelScope.launch(supervisorJob){
            getLetterLevelOptionsUseCase().collectLatest{
                _state.update { state ->
                    state.copy(
                        letterLevelOptions = it
                    )
                }
            }
        }
    }

    private fun loadLetterType(){
        viewModelScope.launch(supervisorJob){
            getLetterTypeOptionsUseCase().collectLatest{
                _state.update { state ->
                    state.copy(
                        letterTypeOptions = it
                    )
                }
            }
        }
    }

    private fun onSearch(keyword: String) {
        _state.update { state -> state.copy(searchKeyword = keyword) }
    }

    private fun handleLoadData() {
        viewModelScope.launch(supervisorJob) {
            loadData()
        }
    }

    private suspend fun loadData() {
        getReqDocsUseCase(
            state.value.searchKeyword,
            state.value.filterLetterTypeId,
            state.value.filterLetterLevel
        ).collectLatest {
            _pagingFlow.emit(it)
        }
    }

    override fun onCleared() {
        super.onCleared()
        supervisorJob.cancel()
    }

}

data class ReqDocUiState(
    val filterLetterLevel: String? = null,
    val filterLetterTypeId: Int? = null,
    val searchKeyword: String = "",
    val filterActive: Int = 0,
    val letterTypeOptions: Resource<InputOptions> = Resource.Idle(),
    val letterLevelOptions: Resource<InputOptions> = Resource.Idle(),
)

sealed class ReqDocUiAction{
    data class Search(val keyword: String) : ReqDocUiAction()
    data class SetFilterLetterType(val value: String?) : ReqDocUiAction()
    data class SetFilterLetterLevel(val value: String?) : ReqDocUiAction()
    data object Filter : ReqDocUiAction()
    data object LoadData: ReqDocUiAction()
    data object LoadLetterType: ReqDocUiAction()
    data object LoadLetterLevel: ReqDocUiAction()
    data object ResetFilter: ReqDocUiAction()
}