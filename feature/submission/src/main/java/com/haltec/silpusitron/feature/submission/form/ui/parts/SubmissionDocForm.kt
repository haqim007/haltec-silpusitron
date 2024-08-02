package com.haltec.silpusitron.feature.submission.form.ui.parts

import android.Manifest
import android.app.Activity
import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.ArrowCircleLeft
import androidx.compose.material.icons.outlined.ArrowCircleRight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.haltec.silpusitron.common.di.commonModule
import com.haltec.silpusitron.core.ui.parts.error.ErrorView
import com.haltec.silpusitron.core.ui.theme.SILPUSITRONTheme
import com.haltec.silpusitron.core.ui.util.KoinPreviewWrapper
import com.haltec.silpusitron.core.ui.util.PermissionRequester
import com.haltec.silpusitron.core.ui.util.isPermissionGranted
import com.haltec.silpusitron.feature.submission.R
import com.haltec.silpusitron.feature.submission.common.di.submissionDocModule
import com.haltec.silpusitron.feature.submission.form.ui.SubmissionDocFormArgs
import com.haltec.silpusitron.feature.submission.form.ui.SubmissionDocUiAction
import com.haltec.silpusitron.feature.submission.form.ui.SubmissionDocUiState
import com.haltec.silpusitron.feature.submission.form.ui.submissionDocFormArgsDummy
import com.haltec.silpusitron.feature.submission.form.ui.submissionDocUiStateDummy
import com.haltec.silpusitron.shared.auth.di.authSharedModule
import com.haltec.silpusitron.shared.formprofile.di.formProfileModule
import com.haltec.silpusitron.shared.formprofile.ui.FormProfileScreen
import com.haltec.silpusitron.shared.formprofile.ui.FormProfileUiAction
import com.haltec.silpusitron.shared.formprofile.ui.FormProfileViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import com.haltec.silpusitron.core.ui.R as CoreR

@Composable
fun SubmissionDocForm(
    modifier: Modifier = Modifier,
    args: SubmissionDocFormArgs,
    state: SubmissionDocUiState,
    action: (SubmissionDocUiAction) -> Unit,
    onPermissionDenied: () -> Unit = {}
) {

    val activity = LocalContext.current as Activity

    Column(modifier = modifier) {
        Card(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .drawWithContent {
                    drawContent()
                    drawLine(
                        color = Color.Gray, // Border color
                        start = Offset(0f, size.height), // Start position at bottom left
                        end = Offset(size.width, size.height), // End position at bottom right
                        strokeWidth = 2.dp.toPx() // Border width
                    )
                },
            colors = CardDefaults.cardColors().copy(
                containerColor = MaterialTheme.colorScheme.background
            ),
            shape = RectangleShape,
            elevation = CardDefaults.elevatedCardElevation(
                defaultElevation = 10.dp
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = args.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = stringResource(R.string.letter_level_n, args.letterLevel),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                )
                Text(
                    text = stringResource(R.string.letter_type_n, args.letterType),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }
        }

        val stepperScrollState = rememberScrollState()
        val scope = rememberCoroutineScope()
        // A callback to handle manual scroll synchronization
        val stepperSynchronizedScrollModifier = Modifier.pointerInput(Unit) {
            detectHorizontalDragGestures { change, _ ->
                scope.launch {
                    stepperScrollState.scrollTo(
                        stepperScrollState.value + change.positionChange().x.toInt()
                    )
                    change.consume()
                }

            }
        }
        Row(
            modifier = Modifier
                .wrapContentWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .horizontalScroll(stepperScrollState)
                .then(stepperSynchronizedScrollModifier)
            ,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.person),
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 16.dp)
                    .size(30.dp)
            )
            Box(
                modifier = Modifier
                    .width(120.dp)
                    .height(5.dp)
                    .background(
                        if (state.stepperIndex == 0)
                            Color(0XFF6AB3EC)
                        else
                            Color(0XFF448BC3)
                    )

            )
            Image(
                painter = painterResource(id = R.drawable.doc_icon_stepper),
                contentDescription = null,
                modifier = Modifier.size(30.dp),
                alpha = if (state.stepperIndex >= 1) 1f else 0.75f
            )
            Box(
                modifier = Modifier
                    .width(125.dp)
                    .height(5.dp)
                    .background(
                        if (state.stepperIndex <= 1)
                            Color(0XFF6AB3EC)
                        else
                            Color(0XFF448BC3)
                    )

            )
            Image(
                painter = painterResource(id = R.drawable.form_icon_stepper),
                contentDescription = null,
                modifier = Modifier.size(30.dp),
                alpha = if (state.stepperIndex >= 2) 1f else 0.75f
            )
        }
        Row(
            modifier = Modifier
                .wrapContentWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 8.dp)
                .horizontalScroll(stepperScrollState),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(45.dp)
        ) {
            Text(
                text = stringResource(R.string.personal_data),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = if (state.stepperIndex == 0) FontWeight.Bold else FontWeight.Normal
            )
            Text(
                text = stringResource(R.string.requirement_docs),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = if (state.stepperIndex == 1) FontWeight.Bold else FontWeight.Normal
            )
            Text(
                text = stringResource(R.string.form),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = if (state.stepperIndex == 2) FontWeight.Bold else FontWeight.Normal
            )
        }
        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            val formProfileViewModel: FormProfileViewModel = koinViewModel()
            val formProfileState by formProfileViewModel.state.collectAsState()
            var hasLoadForm by remember { mutableStateOf(false) }

            LaunchedEffect(key1 = formProfileState.isAllValid) {
                if (formProfileState.isAllValid == true) {
                    action(SubmissionDocUiAction.ToNextStepper)
                    action(SubmissionDocUiAction.SetProfileData(formProfileState.inputs))
                    formProfileViewModel.doAction(FormProfileUiAction.ResetIsAllValidState)
                }
            }

            val permissions = mutableListOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
                permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
            else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                permissions.add(Manifest.permission.READ_MEDIA_IMAGES)
            }
            PermissionRequester(
                permissions = permissions,
                onPermissionGranted = {},
                onPermissionDenied = onPermissionDenied,
            )
            if (state.stepperIndex == 0) {
                FormProfileScreen(
                    hasLoadForm = hasLoadForm,
                    setHasLoadForm = {
                        hasLoadForm = true
                    },
                    isMapPermissionGranted = isPermissionGranted(
                        activity,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) && isPermissionGranted(
                        activity,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ),
                    title = {
                        Text(
                            text = stringResource(id = R.string.personal_data),
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            ),
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                    },
                    state = formProfileState,
                    action = { action -> formProfileViewModel.doAction(action) },
                    withMap = true,
                    additionalContent = {
                        Row(modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                            ButtonNext(
                                onClick = {
                                    formProfileViewModel.doAction(FormProfileUiAction.ValidateAll)
                                }
                            )
                        }
                    }
                )
            }
            else if (state.stepperIndex == 1) {

                FormRequirementDocs(
                    modifier = Modifier
                        .weight(5f)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    state = state,
                    action = action
                )

                Row (
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    ButtonPrev(onClick = {
                        action(SubmissionDocUiAction.BackToPrevStepper)
                    })
                    ButtonNext {
                        action(SubmissionDocUiAction.ValidateAllDocs)
                    }
                }
            }
            else{

                var showDialogAgree by remember {
                    mutableStateOf(false)
                }

                LaunchedEffect(key1 = state.hasAgree, key2 = state.allFormSubmissionValid) {
                    if (!state.hasAgree && state.allFormSubmissionValid){
                        showDialogAgree = true
                    }
                }

                if (showDialogAgree){
                    Dialog(
                        onDismissRequest = { showDialogAgree = false },
                        properties = DialogProperties(
                            dismissOnClickOutside = true,
                            dismissOnBackPress = true
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
                                    modifier = Modifier.padding(horizontal = 16.dp),
                                    message = stringResource(id = R.string.agreement_required),
                                    textStyle = MaterialTheme.typography.bodyLarge.copy(
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        textAlign = TextAlign.Center
                                    )
                                )

                                IconButton(
                                    onClick = {
                                        showDialogAgree = false
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

                FormSubmission(
                    state = state,
                    action = action,
                    additionalContent = {
                        Row(
                            modifier = Modifier
                                .wrapContentHeight()
                                .padding(vertical = 8.dp)
                                .clickable {
                                    action(SubmissionDocUiAction.AgreeToTheTerm(!state.hasAgree))
                                }
                        ) {
                            Checkbox(
                                checked = state.hasAgree,
                                onCheckedChange = {
                                    action(SubmissionDocUiAction.AgreeToTheTerm(it))
                                }
                            )
                            Text(
                                text = stringResource(R.string.privacy_term),
                                style = TextStyle.Default.copy(
                                    fontSize = 12.sp
                                )
                            )
                        }

                        Row (
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .padding(bottom = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ){
                            ButtonPrev(onClick = {
                                action(SubmissionDocUiAction.BackToPrevStepper)
                            })
                            ButtonNext(
                                text = stringResource(id = CoreR.string.save),
                                icon = Icons.AutoMirrored.Outlined.Send,
                                enabled = state.hasAgree
                            ) {
                                action(SubmissionDocUiAction.Submit)
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun PermissionDialog(
    title: String = stringResource(CoreR.string.attention_),
    message: String = stringResource(R.string.permission_to_access_file_location_needed),
    isAllGranted: Boolean,
    onOK: () -> Unit,
    onCancel: () -> Unit
){
    var show by remember {
        mutableStateOf(!isAllGranted)
    }

//    val lifecycleOwner = LocalLifecycleOwner.current
//    val lifecycleState by lifecycleOwner.lifecycle.currentStateFlow.collectAsState()
//
//    LaunchedEffect(lifecycleState) {
//        show = !isAllGranted && lifecycleState == Lifecycle.State.RESUMED
//    }

    if (show){
        AlertDialog(
            properties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false
            ),
            onDismissRequest = {
                show = false
                onOK()
            },
            confirmButton = {
                TextButton(onClick = {
                    show = false
                    onOK()
                }) {
                    Text(stringResource(CoreR.string.allow))
                }
            },
            dismissButton = {
                TextButton(onClick = {
                        show = false
                        onCancel()
                    }
                ) {
                    Text(stringResource(CoreR.string.back))
                }
            },
            icon = {
                Icon(Icons.Default.Warning, contentDescription = null)
            },
            title = {
                Text(text = title)
            },
            text = {
                Text(text = message)
            }
        )
    }
}


@Composable
private fun ButtonPrev(
    onClick: () -> Unit
) {
    Button(
        colors = ButtonDefaults.buttonColors().copy(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground,
        ),
        shape = RoundedCornerShape(5.dp),
        onClick = onClick
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.ArrowCircleLeft,
                contentDescription = null
            )
            Text(text = stringResource(CoreR.string.back))
        }
    }
}


@Composable
private fun ButtonNext(
    text: String = stringResource(CoreR.string.next),
    icon: ImageVector = Icons.Outlined.ArrowCircleRight,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Button(
        modifier = Modifier,
        onClick = onClick,
        colors = ButtonDefaults.buttonColors().copy(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        shape = RoundedCornerShape(5.dp),
        enabled = enabled
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = text,
                fontWeight = FontWeight.Bold
            )
            Icon(
                imageVector = icon,
                contentDescription = null
            )
        }
    }
}

@Preview
@Composable
fun SubmissionDocForm_Preview(){
    KoinPreviewWrapper(modules = listOf(commonModule, formProfileModule, authSharedModule, submissionDocModule)) {
        SILPUSITRONTheme {
            SubmissionDocForm(
                args = submissionDocFormArgsDummy,
                state = submissionDocUiStateDummy,
                action = { _ ->}
            )
        }
    }
}