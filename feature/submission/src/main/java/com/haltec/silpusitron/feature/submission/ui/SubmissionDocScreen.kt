package com.haltec.silpusitron.feature.submission.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.haltec.silpusitron.core.ui.component.LottieLoader
import com.haltec.silpusitron.core.ui.component.SmallTopBar
import com.haltec.silpusitron.core.ui.parts.ErrorView
import com.haltec.silpusitron.core.ui.parts.LoadingView
import com.haltec.silpusitron.core.ui.theme.SILPUSITRONTheme
import com.haltec.silpusitron.core.ui.theme.gradientColors
import com.haltec.silpusitron.core.ui.util.KoinPreviewWrapper
import com.haltec.silpusitron.data.mechanism.Resource
import com.haltec.silpusitron.feature.requirementdocs.common.domain.requirementDocDummies
import com.haltec.silpusitron.feature.submission.R
import com.haltec.silpusitron.feature.submission.di.submissionDocModule
import com.haltec.silpusitron.feature.submission.domain.templateFormDummy
import com.haltec.silpusitron.feature.submission.ui.parts.SubmissionDocForm
import org.koin.androidx.compose.koinViewModel
import com.haltec.silpusitron.core.ui.R as CoreR


@Composable
fun SubmissionDocScreen(
    modifier: Modifier = Modifier,
    viewModel: SubmissionDocViewModel = koinViewModel(),
    onNavigateBack: () -> Unit = {},
    onSucceed: () -> Unit = {},
    args: SubmissionDocFormArgs
){

    val state by viewModel.state.collectAsState()
    val action = {action: SubmissionDocUiAction -> viewModel.doAction(action)}

    LaunchedEffect(key1 = Unit) {
        viewModel.doAction(SubmissionDocUiAction.SetTemplateId(args.id))
    }

    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface)
            .fillMaxSize()
    ) {
        Row(
            Modifier
                .background(
                brush = Brush.linearGradient(
                    gradientColors,
                    start = Offset(0f, Float.POSITIVE_INFINITY),
                    end = Offset(Float.POSITIVE_INFINITY, 1000f),
                ),
                shape = RoundedCornerShape(
                    bottomEnd = 20.dp, bottomStart = 20.dp
                )
            )
        ) {
            IconButton(
                onClick = { onNavigateBack() },
                colors = IconButtonDefaults.iconButtonColors()
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(id = R.string.back),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
            SmallTopBar(
                shape = RoundedCornerShape(0.dp),
                modifier = Modifier.height(50.dp),
            ) {
                Text(
                    text = stringResource(CoreR.string.create_new_submission),
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                )
            }
        }

        when (state.template) {
            is Resource.Success -> {
                SubmissionDocForm(
                    modifier,
                    args,
                    state,
                    action,
                    onNavigateBack
                )
            }

            is Resource.Error -> {
                ErrorView(
                    message = state.template.message,
                    onTryAgain = { action(SubmissionDocUiAction.GetTemplate) }
                )
            }

            else -> {
                LoadingView()
            }
        }

        var showDialogSubmitError by remember {
            mutableStateOf(false)
        }

        when (state.submitResult) {
            is Resource.Success -> {
                onSucceed()
                action(SubmissionDocUiAction.ResetSubmitState)
            }

            is Resource.Error -> {
                showDialogSubmitError = true
            }

            is Resource.Loading -> {
                Dialog(
                    onDismissRequest = {},
                    properties = DialogProperties(
                        dismissOnClickOutside = false,
                        dismissOnBackPress = false
                    )
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(500.dp)
                            .padding(2.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors().copy(
                            containerColor = MaterialTheme.colorScheme.background
                        )
                    ) {
                        LoadingView(
                            loader = {
                                LottieLoader(
                                    jsonRaw = CoreR.raw.lottie_loading_doc,
                                    modifier = Modifier
                                        .padding(bottom = 10.dp)
                                        .size(300.dp)
                                )
                            }
                        )
                    }
                }
            }

            else -> Unit
        }

        if (showDialogSubmitError){
            Dialog(
                onDismissRequest = {
                    showDialogSubmitError = false
                    action(SubmissionDocUiAction.ResetSubmitState)
                 },
                properties = DialogProperties(
                    dismissOnClickOutside = true
                )
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(450.dp)
                        .padding(2.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors().copy(
                        containerColor = MaterialTheme.colorScheme.background
                    )
                ) {
                    Box {
                        ErrorView(
                            modifier = Modifier,
                            state.template.message,
                            onTryAgain = { action(SubmissionDocUiAction.Submit) }
                        )

                        IconButton(
                            onClick = {
                                showDialogSubmitError = false
                                action(SubmissionDocUiAction.ResetSubmitState)
                            },
                            modifier = Modifier.align(Alignment.TopEnd),
                            colors = IconButtonDefaults.iconButtonColors().copy(
                                contentColor = MaterialTheme.colorScheme.error
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = stringResource(id = CoreR.string.close))
                        }
                    }
                }
            }
        }

    }
}


@Preview
@Composable
fun SubmissionDocForm_Preview(){
    SILPUSITRONTheme {
        val doc = requirementDocDummies[0]
        SubmissionDocScreen(
            args = SubmissionDocFormArgs(
                title = doc.title,
                id = doc.id,
                letterType = doc.letterType,
                letterLevel = doc.letterLevel
            )
        )
    }
}

@Preview(name = "berkas persyaratan lengkap")
@Composable
fun SubmissionDocFormDocFileComplete_Preview(){
    KoinPreviewWrapper(modules = listOf(submissionDocModule)) {
        SILPUSITRONTheme {
            val doc = requirementDocDummies[0]
            val viewModel: SubmissionDocViewModel = koinViewModel()
            LaunchedEffect(key1 = Unit) {
                viewModel.doAction(
                    SubmissionDocUiAction.SetState(
                        submissionDocUiStateDummy.copy(
                            requirementDocs = mapOf(),
                            stepperIndex = 1,
                            template = Resource.Success(templateFormDummy)
                        )
                    )
                )
            }
            SubmissionDocScreen(
                args = SubmissionDocFormArgs(
                    title = doc.title,
                    id = doc.id,
                    letterType = doc.letterType,
                    letterLevel = doc.letterLevel
                ),
                viewModel = viewModel
            )
        }
    }
}