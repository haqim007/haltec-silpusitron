package com.silpusitron.feature.submissionhistory.histories.parts

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.silpusitron.core.ui.component.LottieLoader
import com.silpusitron.core.ui.theme.BackgroundLight
import com.silpusitron.core.ui.theme.SILPUSITRONTheme
import com.silpusitron.feature.submissionhistory.common.domain.SubmissionHistory
import com.silpusitron.feature.submissionhistory.common.domain.SubmissionHistoryDummies
import com.silpusitron.core.ui.R as CoreR


@Composable
fun HistoryItemView(
    data: SubmissionHistory,
    modifier : Modifier = Modifier,
    onClick: (data: SubmissionHistory) -> Unit,
){
    var showAlert by remember {
        mutableStateOf(false)
    }

    if (showAlert){
        AlertDialog(
            onDismissRequest = { showAlert = false },
            confirmButton = {
                TextButton(onClick = { showAlert = false }) {
                    Text(text = stringResource(id = CoreR.string.ok))
                }
            },
            icon = {
                LottieLoader(
                    jsonRaw = CoreR.raw.lottie_questioning,
                    modifier = Modifier.size(150.dp),
                    iterations = 1
                )
            },
            text = {
                Text(stringResource(id = CoreR.string.submission_in_progress))
            }
        )
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable {
                if (data.isFinish || data.isEditable) onClick(data)
                else showAlert = true
            },
        border = BorderStroke(0.25.dp, MaterialTheme.colorScheme.secondary),
        colors = CardDefaults.cardColors().copy(
            containerColor = BackgroundLight
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.5.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
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
                text = stringResource(CoreR.string.submitted_at_s, data.submissionDate),
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal
                )
            )
            Text(
                text = stringResource(CoreR.string.proceed_by_s, data.proceedBy),
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal
                )
            )

            val annotatedString = buildAnnotatedString {
                pushStyle(SpanStyle(fontSize = 14.sp)) // Set base style
                append(stringResource(id = CoreR.string.the_current_status))
                pushStyle(SpanStyle(fontWeight = FontWeight.Bold)) // Set bold style
                append(" ${data.statusLabel}")
                pop()
            }
            Text(
                text = annotatedString,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal
                )
            )
        }
    }
}

@Preview
@Composable
fun HistoryItemView_Preview(){
    SILPUSITRONTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            HistoryItemView(
                data = SubmissionHistoryDummies[0],
                onClick = {},
            )
        }
    }
}