package com.silpusitron.feature.officertask.tasks.ui

import android.util.Log
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.silpusitron.core.ui.ui.BaseViewModel
import com.silpusitron.data.mechanism.Resource
import com.silpusitron.feature.officertask.common.domain.RejectingUseCase
import com.silpusitron.feature.officertask.common.domain.SigningUseCase
import com.silpusitron.feature.officertask.docapproval.DocPreviewApprovalUiAction
import com.silpusitron.feature.officertask.tasks.domain.GetSubmittedLettersUseCase
import com.silpusitron.feature.officertask.tasks.domain.SubmittedLetter
import com.silpusitron.shared.form.domain.model.InputOptions
import com.silpusitron.shared.lettertype.domain.GetLetterTypeOptionsUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class OfficerTasksViewModel(
    private val getSubmittedLettersUseCase: GetSubmittedLettersUseCase,
    private val getLetterTypeOptionsUseCase: GetLetterTypeOptionsUseCase,
    private val signingUseCase: SigningUseCase,
    private val rejectingUseCase: RejectingUseCase
): BaseViewModel<OfficerTasksUiState, OfficerTasksUiAction>() {
    override val _state = MutableStateFlow(OfficerTasksUiState())

    private val _pagingFlow = MutableStateFlow<PagingData<SubmittedLetter>>(PagingData.empty())
    val pagingFlow: Flow<PagingData<SubmittedLetter>> = _pagingFlow
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

        viewModelScope.launch {
            state.map { it.selectedIds }.collectLatest {
                _state.update { state ->
                    state.copy(selectMultipleActive = it.isNotEmpty())
                }
            }
        }
    }

    override fun doAction(action: OfficerTasksUiAction) {
        when(action){
            OfficerTasksUiAction.LoadData -> loadData()
            OfficerTasksUiAction.LoadLetterType -> loadLetterType()
            OfficerTasksUiAction.ResetFilter -> resetFilter()
            is OfficerTasksUiAction.Search -> search(action.keyword)
            is OfficerTasksUiAction.SetFilter -> onFilter(
                action.startDate, action.endDate,
                action.letterType, action.letterStatus
            )
            is OfficerTasksUiAction.SetDummyPagingData -> {
                viewModelScope.launch {
                    _pagingFlow.emit(PagingData.from(action.data))
                }
            }
            is OfficerTasksUiAction.SelectId -> onSelectId(action.id)
            is OfficerTasksUiAction.Rejecting -> rejecting(action.reason)
            is OfficerTasksUiAction.Signing -> signing(action.passphrase)
            OfficerTasksUiAction.ResetSigningResult -> _state.update { state ->
                state.copy(signingResult = Resource.Idle())
            }
            OfficerTasksUiAction.DeselectAll -> deselectAll()
            OfficerTasksUiAction.SelectAll -> selectAll()

            is OfficerTasksUiAction.SetDummyState -> _state.update { action.state }
            is OfficerTasksUiAction.AddDisplayedId -> addDisplayedId(action.id)
        }
    }

    private fun addDisplayedId(id: Int){
        _state.update { state ->
            state.copy(
                displayedIds = state.displayedIds + id
            )
        }
    }

    private fun selectAll(){
        _state.update { state ->
            state.copy(
                isSelectAllActive = true,
                selectedIds = state.displayedIds
            )
        }
    }

    private fun deselectAll(){
        _state.update { state ->
            state.copy(
                isSelectAllActive = false,
                selectedIds = setOf()
            )
        }
    }

    private fun rejecting(reason: String){
        viewModelScope.launch {
            rejectingUseCase(
                ids = state.value.selectedIds.toList(),
                reason = reason
            ).collectLatest {
                _state.update { state ->
                    state.copy(signingResult = it)
                }
            }
        }
    }

    private fun signing(passphrase: String){
        viewModelScope.launch {
            signingUseCase(
                ids = state.value.selectedIds.toList(),
                passphrase = passphrase
            ).collectLatest {
                _state.update { state ->
                    state.copy(signingResult = it)
                }
            }

        }
    }

    private fun onSelectId(id: Int) {
        if (state.value.selectedIds.contains(id)) {
            _state.update { state ->
                state.copy(
                    selectedIds = state.selectedIds - id
                )
            }
        } else {
            _state.update { state ->
                state.copy(
                    selectedIds = state.selectedIds + id
                )
            }
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
                filterActive = listFilter.filterNotNull().count()
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

    private fun search(keyword: String) {
        _state.update { state ->
            state.copy(searchKeyword = keyword)
        }
    }

    private fun loadData() {
        if (state.value.isSelectAllActive){
            deselectAll()
        }
        viewModelScope.launch {
            getSubmittedLettersUseCase(
                searchKeyword = state.value.searchKeyword,
                filterStartDate = state.value.filterStartDate,
                filterEndDate = state.value.filterEndDate,
                filterLetterTypeId = state.value.filterLetterTypeId,
            ).collectLatest {
                _pagingFlow.emit(it)
            }
        }
    }
}

sealed class OfficerTasksUiAction{
    data class Search(val keyword: String) : OfficerTasksUiAction()
    data object LoadData: OfficerTasksUiAction()
    data object LoadLetterType: OfficerTasksUiAction()
    data object ResetFilter: OfficerTasksUiAction()
    data class SetFilter(
        val letterType: Int? = null,
        val letterStatus: String? = null,
        val startDate: LocalDateTime? = null,
        val endDate: LocalDateTime? = null
    ): OfficerTasksUiAction()
    data class SelectId(val id: Int): OfficerTasksUiAction()
    data class Signing(val passphrase: String): OfficerTasksUiAction()
    data class Rejecting(val reason: String): OfficerTasksUiAction()
    data object ResetSigningResult: OfficerTasksUiAction()
    data object SelectAll: OfficerTasksUiAction()
    data object DeselectAll: OfficerTasksUiAction()
    data class AddDisplayedId(val id: Int): OfficerTasksUiAction()

    data class SetDummyPagingData(val data: List<SubmittedLetter>): OfficerTasksUiAction()
    data class SetDummyState(val state: OfficerTasksUiState): OfficerTasksUiAction()
}

data class OfficerTasksUiState(
    val filterStartDate: LocalDateTime? = null,
    val filterEndDate: LocalDateTime? = null,
    val filterLetterStatus: String? = null,
    val filterLetterTypeId: Int? = null,
    val searchKeyword: String = "",
    val filterActive: Int = 0,
    val letterTypeOptions: Resource<InputOptions> = Resource.Idle(),
    val selectedIds: Set<Int> = setOf(),
    val selectMultipleActive: Boolean = false,
    val displayedIds: Set<Int> = setOf(),
    val isSelectAllActive: Boolean = false,
    val signingResult: Resource<String> = Resource.Idle()
)