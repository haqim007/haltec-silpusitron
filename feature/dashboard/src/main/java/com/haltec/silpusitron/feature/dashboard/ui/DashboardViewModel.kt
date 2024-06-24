package com.haltec.silpusitron.feature.dashboard.ui

import androidx.lifecycle.viewModelScope
import com.haltec.silpusitron.core.ui.ui.BaseViewModel
import com.haltec.silpusitron.data.mechanism.Resource
import com.haltec.silpusitron.feature.dashboard.domain.model.DashboardData
import com.haltec.silpusitron.feature.dashboard.domain.usecase.GetDashboardDataUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val getDashboardDataUseCase: GetDashboardDataUseCase
) : BaseViewModel<DashboardUiState, DashboardUiAction>() {
    override val _state = MutableStateFlow(DashboardUiState())
    override fun doAction(action: DashboardUiAction) {
        when(action){
            DashboardUiAction.GetData -> getDashboardData()
        }
    }

    private fun getDashboardData(){
        viewModelScope.launch {
            getDashboardDataUseCase().collectLatest {
                _state.update { state -> state.copy(data = it) }
            }
        }
    }
}

data class DashboardUiState(
    val data: Resource<DashboardData> = Resource.Idle()
)

sealed class DashboardUiAction{
    data object GetData: DashboardUiAction()
}


val dashboardUiStateDummy = DashboardUiState(
        data = Resource.Success(
            data = DashboardData(
                summaries = listOf(
                    DashboardData.Summary(
                        label = "Total Pengajuan Surat",
                        value = 10
                    ),
                    DashboardData.Summary(
                        label = "Total Pengajuan Surat",
                        value = 10
                    ),
                    DashboardData.Summary(
                        label = "Total Pengajuan Surat",
                        value = 10
                    )
                ),
                pieData = listOf(
                    DashboardData.PieData(
                        label = "keterangan",
                        value = 10f
                    ),
                    DashboardData.PieData(
                        label = "keterangan",
                        value = 10f
                    ),
                    DashboardData.PieData(
                        label = "keterangan",
                        value = 10f
                    ),
                    DashboardData.PieData(
                        label = "keterangan",
                        value = 10f
                    ),
                    DashboardData.PieData(
                        label = "Pisang",
                        value = 25f
                    )
                )
            )
        )
    )
