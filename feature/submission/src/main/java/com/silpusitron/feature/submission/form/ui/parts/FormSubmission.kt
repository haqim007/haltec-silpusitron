package com.silpusitron.feature.submission.form.ui.parts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.haltec.silpusitron.core.ui.component.InputLabel
import com.silpusitron.feature.submission.R
import com.silpusitron.feature.submission.form.ui.SubmissionDocUiAction
import com.silpusitron.feature.submission.form.ui.SubmissionDocUiState
import com.silpusitron.shared.form.domain.model.isRequired
import com.silpusitron.shared.form.ui.components.FormTextField


@Composable
fun FormSubmission(
    state: SubmissionDocUiState,
    action: (SubmissionDocUiAction) -> Unit,
    modifier: Modifier = Modifier,
    additionalContent: @Composable (() -> Unit)? = null,
) {
    Column(
        modifier = modifier
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = stringResource(id = R.string.form),
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            ),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        val inputs = state.forms.values.toList()
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(
                count = inputs.size,
                key = {
                    inputs[it].inputName
                }
            ) { it ->
                val item = inputs[it]

                FormTextField(
                    value = item.value ?: "",
                    onValueChange = {
                        action(SubmissionDocUiAction.SetInput(item.inputName.toInt(), it))
                    },
                    label = {
                        InputLabel(
                            label = item.inputLabel,
                            isRequired = item.isRequired()
                        )
                    },
                    inputLabel = item.inputLabel,
                    singleLine = true,
                    inputData = item,
                    modifier = Modifier
                        .padding(bottom = 4.dp)
                        .fillMaxWidth()
                )
            }


            item {
                additionalContent?.let { it() }
            }

        }

    }
}
