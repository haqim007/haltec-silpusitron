package com.silpusitron.feature.dashboard.common.ui.parts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.silpusitron.core.ui.theme.SILPUSITRONTheme
import com.silpusitron.feature.dashboard.R
import com.silpusitron.feature.dashboard.common.domain.model.BarCharts
import com.silpusitron.feature.dashboard.common.domain.model.DashboardChart
import com.silpusitron.feature.dashboard.common.domain.model.DashboardData
import com.silpusitron.feature.dashboard.common.domain.model.PiesData
import com.silpusitron.feature.dashboard.common.domain.model.Summaries
import com.silpusitron.feature.dashboard.common.ui.components.BarChartLegend
import com.silpusitron.feature.dashboard.common.ui.components.BarChartView
import com.silpusitron.feature.dashboard.common.ui.components.PieChartCard
import com.silpusitron.feature.dashboard.common.ui.components.SummaryCard
import com.silpusitron.feature.dashboard.common.ui.components.SummaryCardModel
import com.silpusitron.feature.dashboard.user.ui.dashboardUiStateDummy

@Composable
fun DashboardChartsView(
    data: List<DashboardData>,
    modifier: Modifier = Modifier,
    onTryAgain: () -> Unit
) {
    Column(
        modifier = modifier
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
                            ),
                            onTryAgain = onTryAgain
                        )
                    }
                }
                is PiesData -> {
                    if (chart.type == DashboardChart.INCOMING_LETTER_BY_TYPE){
                        PieChartCard(
                            title = stringResource(R.string.analysis_submission_letter_by_type),
                            data = chart.data,
                            onTryAgain = onTryAgain
                        )
                    }
                    else if(chart.type == DashboardChart.INCOMING_LETTER_BY_STATUS){
                        PieChartCard(
                            title = stringResource(R.string.analysis_submission_letter_by_status),
                            data = chart.data,
                            onTryAgain = onTryAgain
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
        Spacer(modifier = Modifier.height(100.dp))
    }
}

@Preview
@Composable
private fun DashboardChartsView_Preview(){
    SILPUSITRONTheme {
        Column(
            Modifier.verticalScroll(rememberScrollState())
        ) {
            DashboardChartsView(
                data = dashboardUiStateDummy.data.data ?: emptyList(),
                onTryAgain = {  }
            )
        }
    }
}