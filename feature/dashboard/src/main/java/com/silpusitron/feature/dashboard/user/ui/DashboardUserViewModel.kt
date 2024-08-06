package com.silpusitron.feature.dashboard.user.ui

import androidx.lifecycle.viewModelScope
import com.silpusitron.core.ui.ui.BaseViewModel
import com.silpusitron.data.mechanism.Resource
import com.silpusitron.feature.dashboard.common.domain.model.BarCharts
import com.silpusitron.feature.dashboard.common.domain.model.DashboardChart
import com.silpusitron.feature.dashboard.common.domain.model.DashboardData
import com.silpusitron.feature.dashboard.common.domain.model.NewsImage
import com.silpusitron.feature.dashboard.common.domain.model.PiesData
import com.silpusitron.feature.dashboard.common.domain.model.Summaries
import com.silpusitron.feature.dashboard.exposed.ui.DashboardExposedUiAction
import com.silpusitron.feature.dashboard.user.domain.usecase.GetDashboardUserUseCase
import com.silpusitron.feature.dashboard.user.domain.usecase.GetNewsImagesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DashboardUserViewModel(
    private val getDashboardUserUseCase: GetDashboardUserUseCase,
    private val getNewsImagesUseCase: GetNewsImagesUseCase
) : BaseViewModel<DashboardUserUiState, DashboardUserUiAction>() {
    override val _state = MutableStateFlow(DashboardUserUiState())
    override fun doAction(action: DashboardUserUiAction) {
        when(action){
            DashboardUserUiAction.GetData -> getDashboardData()
            is DashboardUserUiAction.SetDummyState -> _state.update { action.state }
            DashboardUserUiAction.LoadNews -> loadNewsImages()
            DashboardUserUiAction.HideNewsDialog -> {
                _state.update { state -> state.copy(showNewsDialog = false) }
            }
            DashboardUserUiAction.ShowNewsDialog -> {
                _state.update { state -> state.copy(showNewsDialog = true) }
            }
        }
    }

    private fun getDashboardData(){
        viewModelScope.launch {
            getDashboardUserUseCase().collectLatest {
                _state.update { state -> state.copy(data = it) }
            }
        }
    }

    private fun loadNewsImages() {
        if (state.value.news !is Resource.Success){
            viewModelScope.launch {
                getNewsImagesUseCase().collectLatest {
                    _state.update { state -> state.copy(news = it) }
                }
            }
        }
    }
}

data class DashboardUserUiState(
    val news: Resource<List<NewsImage>> = Resource.Idle(),
    val data: Resource<List<DashboardData>> = Resource.Idle(),
    val showNewsDialog: Boolean? = null
)

sealed class DashboardUserUiAction{
    data object GetData: DashboardUserUiAction()
    data object LoadNews: DashboardUserUiAction()
    data object ShowNewsDialog: DashboardUserUiAction()
    data object HideNewsDialog: DashboardUserUiAction()

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
                            label = "keterangan keterangan keterangan keterangan keterangan",
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
