package com.silpusitron.feature.dashboard.common.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.yml.charts.axis.AxisData
import co.yml.charts.common.components.Legends
import co.yml.charts.common.model.LegendLabel
import co.yml.charts.common.model.LegendsConfig
import co.yml.charts.common.model.Point
import co.yml.charts.ui.barchart.GroupBarChart
import co.yml.charts.ui.barchart.models.BarData
import co.yml.charts.ui.barchart.models.BarPlotData
import co.yml.charts.ui.barchart.models.BarStyle
import co.yml.charts.ui.barchart.models.GroupBar
import co.yml.charts.ui.barchart.models.GroupBarChartData
import co.yml.charts.ui.barchart.models.SelectionHighlightData
import com.silpusitron.core.ui.theme.SILPUSITRONTheme
import com.silpusitron.feature.dashboard.R
import com.silpusitron.feature.dashboard.common.domain.model.BarCharts

data class BarChartLegend(
    val name: String,
    val color: Color
)

@Composable
fun BarChartView(
    title: String,
    data:  List<BarCharts.BarChartsData>,
    @SuppressLint("ModifierParameter")
    modifier: Modifier = Modifier,
    legends: List<BarChartLegend> = listOf()
){

    var maxY by remember {
        mutableIntStateOf(0)
    }

    LaunchedEffect(key1 = Unit) {
        maxY = data.flatMap { it.bars }.maxOfOrNull { it.toInt() } ?: 0
    }

    Card(
        modifier = Modifier.padding(top = 24.dp),
        colors = CardDefaults.cardColors().copy(
            containerColor = MaterialTheme.colorScheme.background
        )
    ) {

        Column(
            modifier = modifier
        ) {

            Text(
                text = title, //stringResource(R.string.service_ratio),
                style = MaterialTheme.typography.labelLarge.copy(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier
                    .padding(top = 16.dp, start = 8.dp)
            )

            val groupBarData = data.mapIndexed { index, it ->
                GroupBar(
                    label = it.label,
                    barList = legends.mapIndexed { legendIndex, legend ->
                        BarData(
                            label = legend.name,
                            point = Point(
                                index.toFloat(),
                                it.bars[legendIndex],
                                "${legend.name}: ${it.bars[legendIndex]}"
                            )
                        )
                    }
                )
            }

            val groupBarPlotData = BarPlotData(
                groupBarList = groupBarData,
                barColorPaletteList = legends.map { it.color },
                barStyle = BarStyle(
                    barWidth = 25.dp,
                    selectionHighlightData = SelectionHighlightData(
                        isHighlightFullBar = true,
                        groupBarPopUpLabel = { name, value ->
                            String.format("%.2f", value)
                        },
                        highlightTextBackgroundColor = MaterialTheme.colorScheme.background,
                        highlightTextColor = MaterialTheme.colorScheme.onBackground
                    ),
                    paddingBetweenBars = 40.dp
                )
            )

            val xAxisData = AxisData.Builder()
                .startDrawPadding(20.dp)
                .axisOffset(10.dp)
                .steps(groupBarData.size - 1)
                .labelAndAxisLinePadding(20.dp)
                .labelData { index -> groupBarData[index].label }
                .backgroundColor(MaterialTheme.colorScheme.background)
                .build()

            val yAxisData = AxisData.Builder()
                .steps(1)
                .labelAndAxisLinePadding(10.dp)
                .axisOffset(20.dp)
                .labelData { index -> (index * (maxY / 1)).toString() }
                .backgroundColor(MaterialTheme.colorScheme.background)
                .build()

            val groupBarChartData = GroupBarChartData(
                barPlotData = groupBarPlotData,
                xAxisData = xAxisData,
                yAxisData = yAxisData,
            )

            Column(
                Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .height(350.dp)
            ) {
                GroupBarChart(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.background)
                        .height(300.dp)
                        .padding(top = 10.dp),
                    groupBarChartData = groupBarChartData
                )

                val legendsConfig = LegendsConfig(
                    legendLabelList = legends.map {
                        LegendLabel(
                            color = it.color,
                            name = it.name
                        )
                    },
                    gridColumnCount = legends.size,
                    spaceBWLabelAndColorBox = 3.dp
                )
                Legends(
                    modifier = Modifier.fillMaxWidth(),
                    legendsConfig = legendsConfig
                )
            }
        }
    }
}

@Preview
@Composable
private fun BarChartViewPreview(){
    SILPUSITRONTheme {
        BarChartView(
            title = stringResource(id = R.string.service_ratio),
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
            ),
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