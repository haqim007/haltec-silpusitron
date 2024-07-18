package com.haltec.silpusitron.feature.submission.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
                    state.template.message,
                    onTryAgain = { action(SubmissionDocUiAction.GetTemplate) }
                )
            }

            else -> {
                LoadingView()
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