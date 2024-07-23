package com.haltec.silpusitron.feature.dashboard.user.ui

import androidx.lifecycle.viewModelScope
import com.haltec.silpusitron.core.ui.ui.BaseViewModel
import com.haltec.silpusitron.data.mechanism.Resource
import com.haltec.silpusitron.feature.dashboard.common.domain.model.BarCharts
import com.haltec.silpusitron.feature.dashboard.common.domain.model.DashboardChart
import com.haltec.silpusitron.feature.dashboard.common.domain.model.DashboardData
import com.haltec.silpusitron.feature.dashboard.common.domain.model.PiesData
import com.haltec.silpusitron.feature.dashboard.common.domain.model.Summaries
import com.haltec.silpusitron.feature.dashboard.user.domain.usecase.GetDashboardUserUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DashboardUserViewModel(
    private val getDashboardUserUseCase: GetDashboardUserUseCase
) : BaseViewModel<DashboardUserUiState, DashboardUserUiAction>() {
    override val _state = MutableStateFlow(DashboardUserUiState())
    override fun doAction(action: DashboardUserUiAction) {
        when(action){
            DashboardUserUiAction.GetData -> getDashboardData()
            is DashboardUserUiAction.SetDummyState -> _state.update { action.state }
        }
    }

    private fun getDashboardData(){
        viewModelScope.launch {
            getDashboardUserUseCase().collectLatest {
                _state.update { state -> state.copy(data = it) }
            }
        }
    }
}

data class DashboardUserUiState(
    val data: Resource<List<DashboardData>> = Resource.Idle()
)

sealed class DashboardUserUiAction{
    data object GetData: DashboardUserUiAction()

    data class SetDummyState(val state: DashboardUserUiState): DashboardUserUiAction()
}


val dashboardUiStateDummy = DashboardUserUiState(
        data = Resource.Success(
            data = listOf(
                Summaries(
                    type = DashboardChart.SUMMARY,
                    data = listOf(
                        Summaries.Summary(
                            label = "Total Pengajuan Surat Total Pengajuan Surat ",
                            value = 10
                        ),
                        Summaries.Summary(
                            label = "Total Pengajuan Surat",
                            value = 10
                        ),
                        Summaries.Summary(
                            label = "Total Pengajuan Surat",
                            value = 10
                        ),
                        Summaries.Summary(
                            label = "Total Pengajuan Surat",
                            value = 90
                        )
                    )
                ),
                BarCharts(
                    type = DashboardChart.SERVICE_RATIO,
                    data = listOf(
                        BarCharts.BarChartsData(
                            label = "Desa 1",
                            bars = listOf(10f, 50f)
                        ),
                        BarCharts.BarChartsData(
                            label = "Desa 2",
                            bars = listOf(90f, 10f)
                        ),
                        BarCharts.BarChartsData(
                            label = "Desa 3",
                            bars = listOf(30f, 60f)
                        ),
                    )
                ),
                PiesData(
                    type = DashboardChart.INCOMING_LETTER_BY_TYPE,
                    data = listOf(
                        PiesData.PieData(
                            label = "keterangan",
                            value = 10f
                        ),
                        PiesData.PieData(
                            label = "keterangan",
                            value = 10f
                        ),
                        PiesData.PieData(
                            label = "keterangan",
                            value = 10f
                        ),
                        PiesData.PieData(
                            label = "keterangan",
                            value = 10f
                        ),
                        PiesData.PieData(
                            label = "Pisang",
                            value = 25f
                        )
                    )
                ),
                PiesData(
                    type = DashboardChart.INCOMING_LETTER_BY_STATUS,
                    data = listOf(
                        PiesData.PieData(
                            label = "keterangan",
                            value = 10f
                        ),
                        PiesData.PieData(
                            label = "keterangan",
                            value = 10f
                        ),
                        PiesData.PieData(
                            label = "keterangan",
                            value = 10f
                        ),
                        PiesData.PieData(
                            label = "keterangan",
                            value = 10f
                        ),
                        PiesData.PieData(
                            label = "Pisang",
                            value = 25f
                        )
                    )
                )
            )
        )
    )
