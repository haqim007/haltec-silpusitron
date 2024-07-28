package com.haltec.silpusitron.feature.submissionhistory.histories

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.haltec.silpusitron.core.ui.ui.BaseViewModel
import com.haltec.silpusitron.data.mechanism.Resource
import com.haltec.silpusitron.feature.submissionhistory.common.domain.SubmissionHistory
import com.haltec.silpusitron.feature.submissionhistory.common.domain.usecase.GetLetterStatusOptionsUseCase
import com.haltec.silpusitron.feature.submissionhistory.common.domain.usecase.GetLetterTypeOptionsUseCase
import com.haltec.silpusitron.feature.submissionhistory.common.domain.usecase.GetSubmissionHistoriesUseCase
import com.haltec.silpusitron.shared.form.domain.model.InputOptions
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime

@OptIn(FlowPreview::class)
class SubmissionHistoriesViewModel(
    private val getSubmissionHistoriesUseCase: GetSubmissionHistoriesUseCase,
    private val getLetterTypeOptionsUseCase: GetLetterTypeOptionsUseCase,
    private val getLetterStatusOptionsUseCase: GetLetterStatusOptionsUseCase
): BaseViewModel<HistoryListUiState, HistoryListUiAction>() {
    override val _state = MutableStateFlow(HistoryListUiState())

    private val _pagingFlow = MutableStateFlow<PagingData<SubmissionHistory>>(PagingData.empty())
    val pagingFlow: Flow<PagingData<SubmissionHistory>> = _pagingFlow
        .asStateFlow()
        .cachedIn(viewModelScope)

    init {
        viewModelScope.launch {
            state.map { it.searchKeyword }.distinctUntilChanged()
                .debounce(1500)
                .collectLatest {
                    loadData()
                }
        }
    }

    override fun doAction(action: HistoryListUiAction) {
        when(action){
            HistoryListUiAction.LoadData -> loadData()
            HistoryListUiAction.LoadLetterStatus -> loadLetterStatus()
            HistoryListUiAction.LoadLetterType -> loadLetterType()
            HistoryListUiAction.ResetFilter -> resetFilter()
            is HistoryListUiAction.Search -> search(action.keyword)
            is HistoryListUiAction.SetFilter -> onFilter(
                action.startDate, action.endDate,
                action.letterType, action.letterStatus
            )

            is HistoryListUiAction.SetDummyPagingData -> {
                viewModelScope.launch() {
                    _pagingFlow.emit(PagingData.from(action.data))
                }
            }
            is HistoryListUiAction.SetDummyState -> _state.update { action.state }
        }
    }

    private fun onFilter(
        startDate: LocalDateTime?, endDate: LocalDateTime?,
        letterType: Int?, letterStatus: String?
    ) {
        val listFilter = listOf(startDate, endDate, letterType, letterStatus)
        _state.update {state ->
            state.copy(
                filterLetterTypeId = letterType,
                filterLetterStatus = letterStatus,
                filterStartDate = startDate,
                filterEndDate = endDate,
                filterActive = listFilter.filter { it != null }.count()
            )
        }
        loadData()
    }

    private fun resetFilter(){
        _state.update {state ->
            state.copy(
                filterLetterTypeId = null,
                filterLetterStatus = null,
                filterStartDate = null,
                filterEndDate = null,
                filterActive = 0
            )
        }
        loadData()
    }

    private fun loadLetterType(){
        viewModelScope.launch{
            getLetterTypeOptionsUseCase().collectLatest{
                _state.update { state ->
                    state.copy(
                        letterTypeOptions = it
                    )
                }
            }
        }
    }

    private fun loadLetterStatus(){
        viewModelScope.launch{
            getLetterStatusOptionsUseCase().collectLatest{
                _state.update { state ->
                    state.copy(
                        letterStatusOptions = it
                    )
                }
            }
        }
    }

    private fun search(keyword: String) {
        _state.update { state ->
            state.copy(searchKeyword = keyword)
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            getSubmissionHistoriesUseCase(
                searchKeyword = state.value.searchKeyword,
                filterStartDate = state.value.filterStartDate,
                filterEndDate = state.value.filterEndDate,
                filterLetterTypeId = state.value.filterLetterTypeId,
                filterLetterStatus = state.value.filterLetterStatus
            ).collectLatest {
                _pagingFlow.emit(it)
            }
        }
    }

}

sealed class HistoryListUiAction{
    data class Search(val keyword: String) : HistoryListUiAction()
    data object LoadData: HistoryListUiAction()
    data object LoadLetterType: HistoryListUiAction()
    data object LoadLetterStatus: HistoryListUiAction()
    data object ResetFilter: HistoryListUiAction()
    data class SetFilter(
        val letterType: Int? = null,
        val letterStatus: String? = null,
        val startDate: LocalDateTime? = null,
        val endDate: LocalDateTime? = null
    ): HistoryListUiAction()

    data class SetDummyPagingData(val data: List<SubmissionHistory>): HistoryListUiAction()
    data class SetDummyState(val state: HistoryListUiState): HistoryListUiAction()
}

internal typealias FileUrl = String
data class HistoryListUiState(
    val filterStartDate: LocalDateTime? = null,
    val filterEndDate: LocalDateTime? = null,
    val filterLetterStatus: String? = null,
    val filterLetterTypeId: Int? = null,
    val searchKeyword: String = "",
    val filterActive: Int = 0,
    val letterTypeOptions: Resource<InputOptions> = Resource.Idle(),
    val letterStatusOptions: Resource<InputOptions> = Resource.Idle()
)