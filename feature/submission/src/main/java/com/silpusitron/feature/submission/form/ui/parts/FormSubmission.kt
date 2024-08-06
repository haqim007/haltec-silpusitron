package com.silpusitron.feature.submission.form.ui.parts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.silpusitron.core.ui.component.InputLabel
import com.silpusitron.core.ui.parts.SubmitSuccessView
import com.silpusitron.core.ui.theme.SILPUSITRONTheme
import com.silpusitron.core.ui.theme.SuccessColor
import com.silpusitron.core.ui.util.KoinPreviewWrapper
import com.silpusitron.common.di.commonModule
import com.silpusitron.feature.submission.R
import com.silpusitron.feature.submission.common.di.submissionDocModule
import com.silpusitron.feature.submission.form.ui.SubmissionDocUiAction
import com.silpusitron.feature.submission.form.ui.SubmissionDocUiState
import com.silpusitron.feature.submission.form.ui.submissionDocFormArgsDummy
import com.silpusitron.feature.submission.form.ui.submissionDocUiStateDummy
import com.silpusitron.shared.auth.di.authSharedModule
import com.silpusitron.shared.form.domain.model.isRequired
import com.silpusitron.shared.form.ui.components.FormTextField
import com.silpusitron.shared.formprofile.di.formProfileModule


@Composable
fun FormSubmission(
    state: SubmissionDocUiState,
    action: (SubmissionDocUiAction) -> Unit,
    modifier: Modifier = Modifier,
    additionalContent: @Composable (() -> Unit)? = null,
) {
    if (state.forms.isEmpty()) {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SubmitSuccessView(
                borderColor = Color.Unspecified,
                text = {
                    Text(
                        text = stringResource(id = R.string.doc_has_complete),
                        style = TextStyle.Default.copy(
                            color = SuccessColor,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
            )
        }
    } else {
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
}

@Composable
fun FormSubmission_Preview(){
    KoinPreviewWrapper(modules = listOf(commonModule, formProfileModule, authSharedModule, submissionDocModule)) {
        SILPUSITRONTheme {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ){
                FormSubmission(
                    state = submissionDocUiStateDummy,
                    action = { _ ->}
                )
            }
        }
    }
}

@Preview(name = "empty forms", apiLevel = 34)
@Composable
fun FormSubmissionEmpty_Preview(){
    KoinPreviewWrapper(modules = listOf(commonModule, formProfileModule, authSharedModule, submissionDocModule)) {
        SILPUSITRONTheme {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ){
                FormSubmission(
                    state = submissionDocUiStateDummy.copy(
                        forms = mapOf()
                    ),
                    action = { _ ->}
                )
            }
        }
    }
}