package com.haltec.silpusitron.feature.dashboard.ui.parts

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.haltec.silpusitron.core.ui.extension.surroundInnerShadow
import com.haltec.silpusitron.core.ui.theme.BackgroundLight
import com.haltec.silpusitron.feature.dashboard.R
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
fun SummaryCard(data: List<SummaryCardModel>) {
    Column {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.statistic_summary),
            style = MaterialTheme.typography.labelLarge.copy(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        )

        Row(
            modifier = Modifier.padding(top = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            data.map { item ->
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .height(150.dp)
                        .surroundInnerShadow(
                            shape = RoundedCornerShape(15.dp),
                            blur = 5.dp,
                            spread = 1.dp
                        ),
                    colors = CardDefaults.cardColors().copy(
                        containerColor = BackgroundLight
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
                        verticalArrangement = Arrangement.spacedBy(8.dp)
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
                                fontSize = 10.sp,
                                textAlign = TextAlign.Center
                            )
                        )

                    }
                }
            }
        }
    }
}

