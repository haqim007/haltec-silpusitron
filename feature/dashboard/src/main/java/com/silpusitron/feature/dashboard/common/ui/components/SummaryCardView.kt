package com.silpusitron.feature.dashboard.common.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.MarqueeDefaults.Iterations
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.silpusitron.core.ui.extension.surroundInnerShadow
import com.silpusitron.core.ui.theme.BackgroundLight
import kotlinx.datetime.Clock


data class SummaryCardModel(
    val color: Color,
    val title: String,
    @DrawableRes
    val icon: Int,
    val value: String
) {
    val id: String
        get() = "$title$value${Clock.System.now().nanosecondsOfSecond}"
}

@Composable
fun SummaryCard(
    title: String,
    data: List<SummaryCardModel>
) {
    Column {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = title, //stringResource(R.string.statistic_summary),
            style = MaterialTheme.typography.labelLarge.copy(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        )

        val chunkedData = if (data.size > 3) data.chunked(2) else listOf(data)
        chunkedData.map { chuncked ->
            Row(
                modifier = Modifier.padding(top = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                chuncked.map { item ->
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .height(150.dp)
                            .surroundInnerShadow(
                                shape = RoundedCornerShape(15.dp),
                                blur = 5.dp,
                                spread = 1.dp,
                                color = item.color.copy(alpha = 0.5f)
                            ),
                        colors = CardDefaults.cardColors().copy(
                            containerColor = MaterialTheme.colorScheme.background
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 2.dp
                        ),
                        border = BorderStroke(
                            1.dp,
                            item.color.copy(alpha = 0.5f)
                        )
                    ) {

                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.SpaceAround
                        ) {
                            Image(
                                painter = painterResource(id = item.icon),
                                modifier = Modifier
                                    .padding(top = 6.dp)
                                    .size(50.dp),
                                contentDescription = null
                            )

                            Text(
                                text = item.value,
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 24.sp,
                                    color = item.color,
                                )
                            )

                            Text(
                                text = item.title,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 12.sp,
                                    textAlign = TextAlign.Center,
                                    fontWeight = FontWeight.Bold
                                ),
                                maxLines = 1,
                                modifier = Modifier.basicMarquee(
                                    iterations = Int.MAX_VALUE
                                )
                            )

                        }
                    }
                }
            }
        }
    }
}

