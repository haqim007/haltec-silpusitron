package com.haltec.silpusitron.feature.dashboard.common.ui.components

import android.text.TextUtils
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.haltec.silpusitron.core.ui.generateColorShades
import com.haltec.silpusitron.feature.dashboard.R
import com.haltec.silpusitron.feature.dashboard.common.domain.model.PiesData
import kotlin.math.roundToInt


@Composable
fun PieChartCard(
    title: String,
    data: List<PiesData.PieData>
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
        Column(modifier = Modifier.padding(8.dp)) {
            Text(
                text = title, //stringResource(R.string.analysis_submission_letter_by_type),
                style = MaterialTheme.typography.labelLarge.copy(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier
                    .padding(top = 16.dp)
            )

            Row(
                modifier = Modifier
                    .padding(top = 16.dp)
            ) {
                PieChart(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    pieChartData,
                    pieChartConfig
                )

                Column(
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp)
                    ) {

                        Text(
                            text = stringResource(R.string.total),
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        )
                        Text(
                            "200",
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
                                    .weight(4f),
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

            }
        }
    }
}