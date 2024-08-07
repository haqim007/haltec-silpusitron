package com.silpusitron.feature.dashboard.common.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.yml.charts.common.model.PlotType
import co.yml.charts.ui.piechart.charts.PieChart
import co.yml.charts.ui.piechart.models.PieChartConfig
import co.yml.charts.ui.piechart.models.PieChartData
import com.silpusitron.core.ui.generateColorShades
import com.silpusitron.core.ui.parts.error.EmptyView
import com.silpusitron.feature.dashboard.R
import com.silpusitron.feature.dashboard.common.domain.model.PiesData
import com.silpusitron.feature.dashboard.common.domain.model.total
import kotlin.math.roundToInt


@Composable
fun PieChartCard(
    title: String,
    data: List<PiesData.PieData>,
    onTryAgain: () -> Unit
) {
    val colorShades = generateColorShades(
        MaterialTheme.colorScheme.secondary.toArgb(),
        data.size
    )

    val pieSlices = data.mapIndexed { index, it ->
        PieChartData.Slice(
            label = it.label,
            value = it.value,
            color = Color(colorShades[index])
        )
    }

    val pieChartData = PieChartData(
        slices = pieSlices,
        plotType = PlotType.Pie
    )
    val pieChartConfig = PieChartConfig(
        backgroundColor =  MaterialTheme.colorScheme.background,
        isAnimationEnable = true,
        showSliceLabels = false,
        animationDuration = 1500,
        labelVisible = false,
        sliceLabelTextSize = 10.sp
    )

    Card(
        modifier = Modifier.padding(top = 24.dp),
        colors = CardDefaults.cardColors().copy(
            containerColor = MaterialTheme.colorScheme.background
        )
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge.copy(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier
                    .padding(top = 16.dp)
                    .align(Alignment.Start)
            )

            if (data.isNotEmpty()){
                PieChart(
                    modifier = Modifier
                        .size(225.dp)
                        .padding(top = 16.dp),
                    pieChartData,
                    pieChartConfig
                )

                Column(
                    modifier = Modifier
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                    ) {

                        Canvas(
                            modifier = Modifier
                                .padding(vertical = 6.dp)
                                .size(10.dp)
                                .weight(1f)

                        ) {
                            drawCircle(Color.Unspecified)
                        }

                        Text(
                            text = stringResource(R.string.total),
                            textAlign = TextAlign.Start,
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            ),
                            modifier = Modifier
                                .weight(4f),
                            maxLines = 1
                        )
                        Text(
                            text = data.total.roundToInt().toString(),
                            modifier = Modifier
                                .weight(1f)
                                .wrapContentWidth(),
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        )
                    }

                    pieChartData.slices.forEach {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Canvas(
                                modifier = Modifier
                                    .padding(vertical = 6.dp)
                                    .size(10.dp)
                                    .weight(1f)

                            ) {
                                drawCircle(it.color)
                            }

                            Text(
                                text = it.label,
                                textAlign = TextAlign.Start,
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontSize = 14.sp
                                ),
                                modifier = Modifier
                                    .weight(4f)
                                    .basicMarquee(
                                        iterations = Int.MAX_VALUE
                                    ),
                                maxLines = 1
                            )
                            Text(
                                text = it.value.roundToInt().toString(),
                                modifier = Modifier
                                    .weight(1f)
                                    .wrapContentWidth(),
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontSize = 14.sp
                                )
                            )
                        }
                    }
                }
            }else{
                EmptyView(
                    modifier = Modifier.padding(top = 16.dp),
                    message = stringResource(R.string.no_data_to_be_displayed),
                    onTryAgain = onTryAgain
                )
            }
        }
    }
}