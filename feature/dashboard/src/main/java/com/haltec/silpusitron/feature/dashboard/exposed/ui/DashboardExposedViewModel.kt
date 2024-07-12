package com.haltec.silpusitron.feature.dashboard.exposed.ui

import androidx.lifecycle.viewModelScope
import com.haltec.silpusitron.core.ui.ui.BaseViewModel
import com.haltec.silpusitron.data.mechanism.Resource
import com.haltec.silpusitron.feature.dashboard.common.domain.model.BarCharts
import com.haltec.silpusitron.feature.dashboard.common.domain.model.DashboardChart
import com.haltec.silpusitron.feature.dashboard.common.domain.model.DashboardData
import com.haltec.silpusitron.feature.dashboard.common.domain.model.PiesData
import com.haltec.silpusitron.feature.dashboard.common.domain.model.Summaries
import com.haltec.silpusitron.feature.dashboard.exposed.domain.model.NewsImage
import com.haltec.silpusitron.feature.dashboard.exposed.domain.usecase.GetDashboardExposedUseCase
import com.haltec.silpusitron.feature.dashboard.exposed.domain.usecase.GetNewsImagesUseCase
import com.haltec.silpusitron.shared.district.domain.usecase.GetDistrictsUseCase
import com.haltec.silpusitron.shared.form.domain.model.InputOptions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime

class DashboardExposedViewModel(
    private val getDashboardExposedUseCase: GetDashboardExposedUseCase,
    private val getDistrictsUseCase: GetDistrictsUseCase,
    private val getNewsImagesUseCase: GetNewsImagesUseCase
) : BaseViewModel<DashboardExposedUiState, DashboardExposedUiAction>() {
    override val _state = MutableStateFlow(DashboardExposedUiState())
    override fun doAction(action: DashboardExposedUiAction) {
        when(action){
            DashboardExposedUiAction.GetData -> getDashboardData()
            is DashboardExposedUiAction.SetFilter -> {
                setFilter(action.districtId, action.startDate, action.endDate)
            }

            DashboardExposedUiAction.LoadDistricts -> {
                loadDistrictOptions()
            }

            DashboardExposedUiAction.LoadNews -> {
                loadNewsImages()
            }

            is DashboardExposedUiAction.ShowAppIntro -> {
                if (state.value.alreadyShowAppIntro != true){
                    _state.update { state ->
                        state.copy(
                            showAppIntro = action.show,
                            alreadyShowAppIntro = !action.show
                        )
                    }
                }

            }

            DashboardExposedUiAction.HideNewsDialog -> {
                _state.update { state -> state.copy(showNewsDialog = false) }
            }
            DashboardExposedUiAction.ShowNewsDialog -> {
                _state.update { state -> state.copy(showNewsDialog = true) }
            }
        }
    }

    private fun loadNewsImages() {
        if (state.value.news !is Resource.Success){
            viewModelScope.launch {
                getNewsImagesUseCase().collectLatest {
                    _state.update { state ->
                        state.copy(
                            news = it
                        )
                    }
                }
            }
        }
    }

    private fun setFilter(
        districtId: String? = null,
        startDate: LocalDateTime? = null,
        endDate: LocalDateTime? = null
    ) {
        _state.update { state ->
            state.copy(
                districtId = districtId,
                startDate = startDate,
                endDate = endDate
            )
        }

        val startDateString = startDate?.let {
            "${startDate.year}-" +
            "${startDate.monthNumber.toString().padStart(2, '0')}-" +
            startDate.dayOfMonth.toString().padStart(2, '0')
        }
        val endDateString = endDate?.let {
            "${endDate.year}-" +
            "${endDate.monthNumber.toString().padStart(2, '0')}-" +
            endDate.dayOfMonth.toString().padStart(2, '0')
        }
        getDashboardData(districtId, startDateString, endDateString)
    }

    private fun loadDistrictOptions() {
        viewModelScope.launch {
            getDistrictsUseCase().collectLatest {
                _state.update { state -> state.copy(districtOptions = it) }
            }
        }
    }

    private fun getDashboardData(
        districtId: String? = null,
        startDate: String? = null,
        endDate: String? = null
    ){
        viewModelScope.launch {
            getDashboardExposedUseCase(
                districtId, startDate, endDate
            ).collectLatest {
                _state.update { state -> state.copy(data = it) }
            }
        }
    }
}

data class DashboardExposedUiState(
    val data: Resource<List<DashboardData>> = Resource.Idle(),
    val districtOptions: Resource<InputOptions> = Resource.Idle(),
    val news: Resource<List<NewsImage>> = Resource.Idle(),
    val districtId: String? = null,
    val startDate: LocalDateTime? = null,
    val endDate: LocalDateTime? = null,
    val showAppIntro: Boolean = false,
    val alreadyShowAppIntro: Boolean? = null,
    val showNewsDialog: Boolean? = null
)

sealed class DashboardExposedUiAction{
    data object LoadDistricts: DashboardExposedUiAction()
    data object GetData: DashboardExposedUiAction()
    data class SetFilter(
        val districtId: String? = null,
        val startDate: LocalDateTime? = null,
        val endDate: LocalDateTime? = null
    ): DashboardExposedUiAction()
    data object LoadNews: DashboardExposedUiAction()
    data class ShowAppIntro(val show: Boolean = true): DashboardExposedUiAction()
    data object ShowNewsDialog: DashboardExposedUiAction()
    data object HideNewsDialog: DashboardExposedUiAction()
}


val dashboardUiStateDummy = DashboardExposedUiState(
        data = Resource.Success(
            data = listOf(
                Summaries(
                    type = DashboardChart.SUMMARY,
                    data = listOf(
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
