package com.haltec.silpusitron.feature.submission.ui.parts

import android.Manifest
import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Send
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.haltec.silpusitron.common.di.commonModule
import com.haltec.silpusitron.core.ui.component.LottieLoader
import com.haltec.silpusitron.core.ui.parts.LoadingView
import com.haltec.silpusitron.core.ui.util.KoinPreviewWrapper
import com.haltec.silpusitron.core.ui.util.PermissionRequester
import com.haltec.silpusitron.feature.submission.R
import com.haltec.silpusitron.feature.submission.di.submissionDocModule
import com.haltec.silpusitron.feature.submission.ui.SubmissionDocFormArgs
import com.haltec.silpusitron.feature.submission.ui.SubmissionDocUiAction
import com.haltec.silpusitron.feature.submission.ui.SubmissionDocUiState
import com.haltec.silpusitron.feature.submission.ui.submissionDocFormArgsDummy
import com.haltec.silpusitron.feature.submission.ui.submissionDocUiStateDummy
import com.haltec.silpusitron.shared.auth.di.authSharedModule
import com.haltec.silpusitron.shared.formprofile.di.formProfileModule
import com.haltec.silpusitron.shared.formprofile.ui.FormProfileScreen
import com.haltec.silpusitron.shared.formprofile.ui.FormProfileUiAction
import com.haltec.silpusitron.shared.formprofile.ui.FormProfileViewModel
import org.koin.androidx.compose.koinViewModel
import com.haltec.silpusitron.core.ui.R as CoreR

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SubmissionDocForm(
    modifier: Modifier = Modifier,
    args: SubmissionDocFormArgs,
    state: SubmissionDocUiState,
    action: (SubmissionDocUiAction) -> Unit,
    onPermissionDenied: () -> Unit = {}
) {

    Card(
        modifier = modifier
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
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Text(
                text = stringResource(R.string.letter_level_n, args.letterLevel),
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            )
            Text(
                text = stringResource(R.string.letter_type_n, args.letterType),
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            )
        }
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.person),
            contentDescription = null,
            modifier = Modifier
                .padding(start = 16.dp)
                .size(50.dp)
        )
        Box(
            modifier = Modifier
                .width(95.dp)
                .height(10.dp)
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
            modifier = Modifier.size(50.dp),
            alpha = if (state.stepperIndex >= 1) 1f else 0.75f
        )
        Box(
            modifier = Modifier
                .width(80.dp)
                .height(10.dp)
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
            modifier = Modifier.size(50.dp),
            alpha = if (state.stepperIndex >= 2) 1f else 0.75f
        )
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(45.dp)
    ) {
        Text(
            text = stringResource(R.string.personal_data),
            style = MaterialTheme.typography.labelMedium
        )
        Text(
            text = stringResource(R.string.requirement_docs),
            style = MaterialTheme.typography.labelMedium
        )
        Text(
            text = stringResource(R.string.form),
            style = MaterialTheme.typography.labelMedium
        )
    }
    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        val formProfileViewModel: FormProfileViewModel = koinViewModel()
        val formProfileState by formProfileViewModel.state.collectAsState()

        var hasLoadForm by remember {
            mutableStateOf(false)
        }

        LaunchedEffect(key1 = formProfileState.isAllValid) {
            if (formProfileState.isAllValid == true) {
                action(SubmissionDocUiAction.ToNextStepper)
                action(SubmissionDocUiAction.SetProfileData(formProfileState.inputs))
                formProfileViewModel.doAction(FormProfileUiAction.ResetIsAllValidState)
            }
        }

        var permissionState: MultiplePermissionsState? = null
        val permissions = mutableListOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
             permissions.add(Manifest.permission.READ_MEDIA_IMAGES)
        }
        PermissionRequester(
            permissions = permissions,
            onPermissionGranted = { permissionState = it },
            onPermissionDenied = { permissionState = it },
            onPermissionsRevoked = { permissionState = it },
            onShouldShowRationale = { relaunch ->
                AlertDialog(
                    onDismissRequest = relaunch,
                    confirmButton = {
                        TextButton(onClick = relaunch) {
                            Text(stringResource(CoreR.string.ok))
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = onPermissionDenied) {
                            Text(stringResource(CoreR.string.cancel))
                        }
                    },
                    icon = {
                        Icon(Icons.Default.Warning, contentDescription = null)
                    },
                    title = {
                        Text(text = stringResource(CoreR.string.attention_))
                    },
                    text = {
                        Text(text = stringResource(R.string.permission_to_access_file_location_needed))
                    }
                )
            }
        )
        if (state.stepperIndex == 0) {
            FormProfileScreen(
                hasLoadForm = hasLoadForm,
                setHasLoadForm = {
                    hasLoadForm = true
                },
                mapPermissionState = permissionState,
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
                                formProfileViewModel.doAction(FormProfileUiAction.Submit)
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

            FormSubmission(modifier, state, action)
            var showDialogAgree by remember {
                mutableStateOf(false)
            }
            
            Row(
                modifier = Modifier.clickable {
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
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                ButtonPrev(onClick = {
                    action(SubmissionDocUiAction.BackToPrevStepper)
                })
                ButtonNext(
                    text = stringResource(id = CoreR.string.save),
                    icon = Icons.AutoMirrored.Outlined.Send
                ) {
                    action(SubmissionDocUiAction.ValidateForms)
                    showDialogAgree = !state.hasAgree
                    if (state.hasAgree && state.allFormSubmissionValid){
                        action(SubmissionDocUiAction.Submit)
                    }
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
                            .height(300.dp)
                            .padding(2.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors().copy(
                            containerColor = MaterialTheme.colorScheme.background
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            LottieLoader(
                                modifier = Modifier.size(200.dp),
                                jsonRaw = CoreR.raw.lottie_questioning
                            )

                            Text(text = stringResource(id = R.string.agreement_required))

                            TextButton(
                                modifier = Modifier.fillMaxWidth()
                                    .padding(top = 16.dp),
                                onClick = {showDialogAgree = false}
                            ){
                                Text(
                                    textAlign = TextAlign.End,
                                    text = stringResource(id = CoreR.string.ok))
                            }
                        }
                    }
                }
            }
        }
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
    onClick: () -> Unit
) {
    Button(
        modifier = Modifier,
        onClick = onClick,
        colors = ButtonDefaults.buttonColors().copy(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        shape = RoundedCornerShape(5.dp)
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
        SubmissionDocForm(
            args = submissionDocFormArgsDummy,
            state = submissionDocUiStateDummy,
            action = { _ ->}
        )
    }
}