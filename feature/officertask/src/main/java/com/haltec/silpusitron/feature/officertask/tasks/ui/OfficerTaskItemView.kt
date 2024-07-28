package com.haltec.silpusitron.feature.officertask.tasks.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.haltec.silpusitron.core.ui.R
import com.haltec.silpusitron.core.ui.component.LottieLoader
import com.haltec.silpusitron.core.ui.theme.BackgroundLight
import com.haltec.silpusitron.core.ui.theme.SILPUSITRONTheme
import com.haltec.silpusitron.feature.officertask.tasks.domain.SubmittedLetter
import com.haltec.silpusitron.feature.officertask.tasks.domain.SubmittedLetterDummies
import com.haltec.silpusitron.core.ui.R as CoreR


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OfficerTaskItemView(
    data: SubmittedLetter,
    modifier: Modifier = Modifier,
    onClick: (data: SubmittedLetter) -> Unit,
    onLongClick: (data: SubmittedLetter) -> Unit,
    multipleSelectActive: Boolean,
    onSelect: (data: SubmittedLetter) -> Unit,
    isSelected: Boolean
){
    var showAlert by remember {
        mutableStateOf(false)
    }

    if (showAlert){
        AlertDialog(
            onDismissRequest = { showAlert = false },
            confirmButton = {
                TextButton(onClick = { showAlert = false }) {
                    Text(text = stringResource(id = R.string.ok))
                }
            },
            icon = {
                LottieLoader(
                    jsonRaw = R.raw.lottie_questioning,
                    modifier = Modifier.size(150.dp),
                    iterations = 1
                )
            },
            text = {
                Text(stringResource(id = R.string.doc_is_not_ready_yet))
            }
        )
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .combinedClickable(
                onClick = {
                    if (data.fileUrl != null) onClick(data)
                    else showAlert = true
                },
                onLongClick = {
                    if (data.fileUrl != null) onLongClick(data)
                },
                onLongClickLabel = stringResource(id = R.string.choose_more_than_one)
            ),
        border = BorderStroke(0.25.dp, MaterialTheme.colorScheme.secondary),
        colors = CardDefaults.cardColors().copy(
            containerColor = BackgroundLight
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.5.dp
        )
    ) {
        Box(
        ) {
            if (multipleSelectActive) {
                Checkbox(
                    modifier = Modifier.align(Alignment.TopEnd),
                    checked = isSelected,
                    onCheckedChange = {
                        onSelect(data)
                    }
                )
            }
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                if (!multipleSelectActive) {
                    Image(
                        modifier = Modifier.size(50.dp),
                        painter = painterResource(id = CoreR.drawable.doc_circle_blue),
                        contentDescription = null
                    )
                }

                Column {
                    Text(
                        text = data.number,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            textDecoration = TextDecoration.Underline
                        )
                    )
                    Text(
                        text = data.title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
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
                        text = stringResource(CoreR.string.requested_by, data.requestedBy),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal
                        )
                    )

                    val annotatedString = buildAnnotatedString {
                        pushStyle(SpanStyle(fontSize = 14.sp)) // Set base style
                        append(stringResource(id = R.string.the_current_status))
                        pushStyle(SpanStyle(fontWeight = FontWeight.Bold)) // Set bold style
                        append(" ${data.status}")
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
    }
}

@Preview
@Composable
private fun LetterItemView_Preview(){
    SILPUSITRONTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            OfficerTaskItemView(
                data = SubmittedLetterDummies[0],
                onClick = {},
                onLongClick = {},
                multipleSelectActive = false,
                onSelect = {},
                isSelected = false
            )
        }
    }
}