package com.haltec.silpusitron.feature.dashboard.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.yml.charts.ui.piechart.models.PieChartData
import com.haltec.silpusitron.common.di.commonModule
import com.haltec.silpusitron.core.ui.generateColorShades
import com.haltec.silpusitron.core.ui.parts.ContainerWithBanner
import com.haltec.silpusitron.core.ui.theme.SILPUSITRONTheme
import com.haltec.silpusitron.data.di.dataModule
import com.haltec.silpusitron.data.mechanism.Resource
import com.haltec.silpusitron.feature.dashboard.R
import com.haltec.silpusitron.feature.dashboard.di.dashboardModule
import com.haltec.silpusitron.feature.dashboard.ui.parts.PieChartCard
import com.haltec.silpusitron.feature.dashboard.ui.parts.SummaryCard
import com.haltec.silpusitron.feature.dashboard.ui.parts.SummaryCardModel
import org.koin.compose.KoinApplication


@Composable
fun DashboardScreen(
    modifier: Modifier = Modifier,
    state: State<DashboardUiState>
){

    val pieSlices: List<PieChartData.Slice> = if (state.value.data is Resource.Success){
        val colorShades = generateColorShades(
            MaterialTheme.colorScheme.primary.toArgb(),
            state.value.data.data?.pieData?.size ?: 1
        )
        state.value.data.data?.pieData?.mapIndexed { index, it ->
            PieChartData.Slice(
                label = it.label,
                value = it.value,
                color = Color(colorShades[index])
            )
        } ?: emptyList()
    } else emptyList()
    val summaryData = if (state.value.data is Resource.Success){
        state.value.data.data?.summaries?.mapIndexed { index, it ->
            SummaryCardModel(
                title = it.label,
                value = it.value.toString(),
                color = when(index){
                    0 -> MaterialTheme.colorScheme.primary
                    1 -> Color(0XFFFC9A07)
                    2 -> Color(0xff02A356)
                    3 -> MaterialTheme.colorScheme.error
                    else -> MaterialTheme.colorScheme.primary
                },
                icon = when(index){
                    0 -> R.drawable.ic_envelope
                    1 -> R.drawable.ic_clock
                    2 -> R.drawable.ic_check
                    else -> R.drawable.ic_envelope
                }
            )
        } ?: emptyList()
    } else emptyList()

    ContainerWithBanner(
        containerModifier = modifier
            .fillMaxSize(),
        bannerModifier = Modifier
            .height(242.dp),
        sharedModifier = Modifier,
        withWelcome = true,
        onBannerInvisible = {
            println("onBannerInvisible: $it")
        }
    ) {
        Column(
            modifier = Modifier
                .padding(top = 16.dp, bottom = 100.dp)
                .padding(horizontal = 18.dp)
        ) {

            if (summaryData.isNotEmpty()){
                SummaryCard(summaryData)
            }

            if (pieSlices.isNotEmpty()){
                PieChartCard(pieSlices)
            }

            if (pieSlices.isNotEmpty()){
                PieChartCard(pieSlices)
            }

        }
    }
}


@Preview(showBackground = true)
@Composable
fun DashboardScreenPreview(){
    KoinApplication(application = {
        modules(listOf(commonModule, dataModule, dashboardModule))
    }) {
        SILPUSITRONTheme {
            DashboardScreen(
                state = remember {
                    mutableStateOf(
                        dashboardUiStateDummy
                    )
                }
            )
        }
    }
}
