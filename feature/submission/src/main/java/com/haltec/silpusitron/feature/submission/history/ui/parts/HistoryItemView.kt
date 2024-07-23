package com.haltec.silpusitron.feature.submission.history.ui.parts

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.FileOpen
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.haltec.silpusitron.core.ui.theme.BackgroundLight
import com.haltec.silpusitron.core.ui.theme.SILPUSITRONTheme
import com.haltec.silpusitron.feature.submission.history.domain.SubmissionHistory
import com.haltec.silpusitron.feature.submission.history.domain.SubmissionHistoryDummies


@Composable
fun HistoryItemView(
    data: SubmissionHistory,
    modifier : Modifier = Modifier
){
    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        border = BorderStroke(0.25.dp, MaterialTheme.colorScheme.secondary),
        colors = CardDefaults.cardColors().copy(
            containerColor = BackgroundLight
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.5.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                text = data.number,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Text(
                text = data.title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(bottom = 16.dp),
            )

            Text(
                text = "Diajukan pada ${data.submissionDate}",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal
                )
            )
            Text(
                text = "Diproses oleh ${data.proceedBy}",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal
                )
            )
            Text(
                text = "Status saat ini ${data.status}",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal
                )
            )

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
            ) {
                Button(
                    shape = RoundedCornerShape(5.dp),
                    modifier = Modifier
                        .height(30.dp)
                        .width(150.dp),
                    onClick = { /*TODO*/ }) {
                    Row {
                        Text(
                            text = "Unduh",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 12.sp
                            )
                        )
                        Icon(imageVector = Icons.Default.Download, contentDescription = null)
                    }
                }

                Button(
                    shape = RoundedCornerShape(5.dp),
                    modifier = Modifier
                        .height(30.dp)
                        .width(150.dp),
                    colors = ButtonDefaults.buttonColors().copy(
                        containerColor = Color(0XFFF3C00E),
                        contentColor = MaterialTheme.colorScheme.onSecondary
                    ),
                    onClick = { /*TODO*/ })
                {
                    Row {
                        Text(
                            text = "Detail",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 12.sp
                            )
                        )

                        Icon(imageVector = Icons.Default.FileOpen, contentDescription = null)
                    }
                }

            }

        }
    }
}

@Preview
@Composable
fun ReqDocView_Preview(){
    SILPUSITRONTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            HistoryItemView(
                data = SubmissionHistoryDummies[0]
            )
        }
    }
}