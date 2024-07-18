package com.haltec.silpusitron.feature.submission.ui.parts

import androidx.compose.foundation.layout.Column
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
import com.haltec.silpusitron.feature.submission.R
import com.haltec.silpusitron.feature.submission.ui.SubmissionDocUiAction
import com.haltec.silpusitron.feature.submission.ui.SubmissionDocUiState
import com.haltec.silpusitron.shared.form.domain.model.isRequired
import com.haltec.silpusitron.shared.form.ui.components.FormTextField


@Composable
fun FormSubmission(
    modifier: Modifier,
    state: SubmissionDocUiState,
    action: (SubmissionDocUiAction) -> Unit
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
        LazyColumn {
            items(
                count = inputs.size,
                key = {
                    inputs[it].inputName
                }
            ) {
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

        }
    }
}
