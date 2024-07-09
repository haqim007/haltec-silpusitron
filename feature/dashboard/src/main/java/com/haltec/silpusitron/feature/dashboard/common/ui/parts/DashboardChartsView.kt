package com.haltec.silpusitron.feature.dashboard.common.ui.parts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.haltec.silpusitron.core.ui.theme.SILPUSITRONTheme
import com.haltec.silpusitron.feature.dashboard.R
import com.haltec.silpusitron.feature.dashboard.common.domain.model.BarCharts
import com.haltec.silpusitron.feature.dashboard.common.domain.model.DashboardChart
import com.haltec.silpusitron.feature.dashboard.common.domain.model.DashboardData
import com.haltec.silpusitron.feature.dashboard.common.domain.model.PiesData
import com.haltec.silpusitron.feature.dashboard.common.domain.model.Summaries
import com.haltec.silpusitron.feature.dashboard.common.ui.components.BarChartLegend
import com.haltec.silpusitron.feature.dashboard.common.ui.components.BarChartView
import com.haltec.silpusitron.feature.dashboard.common.ui.components.PieChartCard
import com.haltec.silpusitron.feature.dashboard.common.ui.components.SummaryCard
import com.haltec.silpusitron.feature.dashboard.common.ui.components.SummaryCardModel
import com.haltec.silpusitron.feature.dashboard.user.ui.dashboardUiStateDummy

@Composable
fun DashboardChartsView(
    data: List<DashboardData>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
    ) {
        data.forEach { chart ->
            when (chart) {
                is BarCharts -> {
                    if (chart.type == DashboardChart.SERVICE_RATIO){
                        BarChartView(
                            title = stringResource(id = R.string.service_ratio),
                            data = chart.data,
                            legends = listOf(
                                BarChartLegend(
                                    stringResource(R.string.incoming_letter),
                                    Color(0Xff26A0FC)
                                ),
                                BarChartLegend(
                                    stringResource(R.string.outgoing_letter),
                                    Color(0XFFFC9A07)
                                )
                            )
                        )
                    }
                }
                is PiesData -> {
                    if (chart.type == DashboardChart.INCOMING_LETTER_BY_TYPE){
                        PieChartCard(
                            title = stringResource(R.string.analysis_submission_letter_by_type),
                            data = chart.data
                        )
                    }
                    else if(chart.type == DashboardChart.INCOMING_LETTER_BY_STATUS){
                        PieChartCard(
                            title = stringResource(R.string.analysis_submission_letter_by_status),
                            data = chart.data
                        )
                    }
                }
                is Summaries -> {
                    if (chart.type == DashboardChart.SUMMARY) {
                        SummaryCard(
                            title = stringResource(R.string.statistic_summary),
                            data = chart.data.mapIndexed { index, it ->
                                SummaryCardModel(
                                    title = it.label,
                                    value = it.value.toString(),
                                    color = when (index) {
                                        0 -> MaterialTheme.colorScheme.primary
                                        1 -> Color(0XFFFC9A07)
                                        2 -> Color(0xff02A356)
                                        3 -> MaterialTheme.colorScheme.error
                                        else -> MaterialTheme.colorScheme.primary
                                    },
                                    icon = when (index) {
                                        0 -> R.drawable.ic_envelope
                                        1 -> R.drawable.ic_clock
                                        2 -> R.drawable.ic_check
                                        else -> R.drawable.ic_envelope
                                    }
                                )
                            }
                        )
                    }

                }
            }
        }
    }
}

@Preview
@Composable
private fun DashboardChartsView_Preview(){
    SILPUSITRONTheme {
        DashboardChartsView(data = dashboardUiStateDummy.data.data ?: emptyList())
    }
}