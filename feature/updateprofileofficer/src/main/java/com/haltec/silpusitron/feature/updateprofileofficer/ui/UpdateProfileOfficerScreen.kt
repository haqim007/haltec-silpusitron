package com.haltec.silpusitron.feature.updateprofileofficer.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SaveAs
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.haltec.silpusitron.core.ui.component.InputLabel
import com.haltec.silpusitron.core.ui.parts.SimpleTopAppBar
import com.haltec.silpusitron.core.ui.parts.SubmitSuccessView
import com.haltec.silpusitron.core.ui.parts.dialog.DialogError
import com.haltec.silpusitron.core.ui.parts.dialog.DialogLoadingDocView
import com.haltec.silpusitron.core.ui.parts.error.ErrorView
import com.haltec.silpusitron.core.ui.parts.loading.LoadingView
import com.haltec.silpusitron.core.ui.theme.DisabledInputContainer
import com.haltec.silpusitron.core.ui.theme.SILPUSITRONTheme
import com.haltec.silpusitron.core.ui.util.KoinPreviewWrapper
import com.haltec.silpusitron.data.mechanism.Resource
import com.haltec.silpusitron.feature.updateprofileofficer.di.updateProfileOfficerModule
import com.haltec.silpusitron.feature.updateprofileofficer.domain.FormProfileOfficerInputKey
import com.haltec.silpusitron.shared.form.domain.model.isRequired
import com.haltec.silpusitron.shared.form.domain.model.valueOrEmpty
import com.haltec.silpusitron.shared.form.ui.components.FormTextField
import org.koin.androidx.compose.koinViewModel
import com.haltec.silpusitron.core.ui.R as CoreR
@Composable
fun UpdateProfileOfficerScreen(
    modifier: Modifier = Modifier,
    viewModel: UpdateProfileOfficerViewModel = koinViewModel(),
    onSuccessSubmit: () -> Unit = {},
    onNavigateBack: () -> Unit
){
    val state by viewModel.state.collectAsState()
    val action = { action: ProfileOfficerUiAction -> viewModel.doAction(action)}

    var hasLoadForm by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = Unit) {
        if (!hasLoadForm){
            action(ProfileOfficerUiAction.GetProfileOfficerData)
            hasLoadForm = true
        }
    }

    Scaffold(
        topBar = {
            SimpleTopAppBar(
                title = stringResource(CoreR.string.profile),
                onNavigateBack = onNavigateBack
            )
        }
    ) { innerPadding ->

        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {

            when (state.profileData) {
                is Resource.Success -> {
                    UpdateProfileOfficerForm(state = state, action = action)
                }

                is Resource.Error -> {
                    ErrorView(
                        message = state.profileData.message,
                        onTryAgain = { action(ProfileOfficerUiAction.GetProfileOfficerData) }
                    )
                }

                else -> {
                    LoadingView()
                }
            }

            when (state.submitResult) {
                is Resource.Error -> {
                    DialogError(
                        message = state.submitResult.message,
                        onDismissRequest = {
                            action(ProfileOfficerUiAction.ResetSubmitState)
                        },
                        onTryAgain = {
                            action(ProfileOfficerUiAction.Submit)
                        }
                    )

                }

                is Resource.Idle -> Unit
                is Resource.Loading -> {
                    DialogLoadingDocView(
                        onDismissRequest = {},
                        properties = DialogProperties(
                            dismissOnClickOutside = false,
                            dismissOnBackPress = false
                        )
                    )
                }

                is Resource.Success -> {
                    Dialog(
                        onDismissRequest = {},
                        properties = DialogProperties(
                            dismissOnBackPress = false,
                            dismissOnClickOutside = false
                        )
                    ) {
                        SubmitSuccessView(onComplete = onSuccessSubmit)
                    }
                }
            }

        }
    }
}

@Composable
fun UpdateProfileOfficerForm(
    modifier: Modifier = Modifier,
    state: ProfileOfficerUiState,
    action: (action: ProfileOfficerUiAction) -> Unit,
) {

    val scrollState = rememberScrollState()

    // Auto scroll whenever firstErrorInputKey changes
    LaunchedEffect(key1 = state.firstErrorInputKey) {
        state.inputsCoordinateY[state.firstErrorInputKey]?.let {
            scrollState.animateScrollBy(it)
            action(ProfileOfficerUiAction.ResetFirstErrorInputKey)
        }
    }

    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize()
            .verticalScroll(scrollState)

    ) {
        
        for ((key, input) in state.inputs){
            var values by remember {
                mutableStateOf("")
            }
            FormTextField(
                value = input.valueOrEmpty(),
                onValueChange = { 
                    if (input.enabled){
                        action(ProfileOfficerUiAction.SetInput(key, it))
                        values = it
                    }
                },
                inputLabel = stringResource(id = getLabelInput(key)),
                label = {
                    InputLabel(
                        label = stringResource(id = getLabelInput(key)),
                        isRequired = input.isRequired()
                    )
                },
                inputData = input,
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned {
                        action(
                            ProfileOfficerUiAction.SaveInputViewCoordinateY(
                                key,
                                it.positionInRoot().y
                            )
                        )
                    },
                enabled = input.enabled,
                readOnly = !input.enabled,
                singleLine = true,
                prefix = {
                    if (input.prefix != null){
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .clip(RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp))
                                .background(color = DisabledInputContainer)
                                .padding(start = 3.dp, top = 1.dp, bottom = 1.dp, end = 5.dp)
                        ){
                            Text(text = input.prefix!!, fontSize = 14.sp)
                        }
                    }
                }
            )
        }

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Button(
                modifier = Modifier,
                onClick = { action(ProfileOfficerUiAction.Submit) },
                colors = ButtonDefaults.buttonColors().copy(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                shape = RoundedCornerShape(5.dp),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = stringResource(id = CoreR.string.save),
                        fontWeight = FontWeight.Bold
                    )
                    Icon(
                        imageVector = Icons.Default.SaveAs,
                        contentDescription = null
                    )
                }
            }
        }


    }
}

private fun getLabelInput(key: FormProfileOfficerInputKey): Int{
     return when(key){
         FormProfileOfficerInputKey.NIP -> CoreR.string.nip
         FormProfileOfficerInputKey.NIK -> CoreR.string.nik
         FormProfileOfficerInputKey.NAME -> CoreR.string.name
         FormProfileOfficerInputKey.OPD -> CoreR.string.opd
         FormProfileOfficerInputKey.EMPLOYEE_STATUS -> CoreR.string.employee_status
         FormProfileOfficerInputKey.EMAIL_GOV -> CoreR.string.email_gov
         FormProfileOfficerInputKey.EMAIL -> CoreR.string.email
         FormProfileOfficerInputKey.PHONE_NUMBER -> CoreR.string.phone_number
         FormProfileOfficerInputKey.REGION_NAME -> CoreR.string.region_name
         FormProfileOfficerInputKey.REGION_TYPE -> CoreR.string.region_type
         FormProfileOfficerInputKey.DISTRICT -> CoreR.string.district
         FormProfileOfficerInputKey.SUB_DISTRICT -> CoreR.string.sub_district
     }
}

@Preview
@Composable
private fun ProfileOfficerScreen_Preview(){
    KoinPreviewWrapper(modules = listOf(updateProfileOfficerModule)) {
        SILPUSITRONTheme {
            val viewModel: UpdateProfileOfficerViewModel = koinViewModel()
            LaunchedEffect(key1 = Unit) {
                viewModel.doAction(
                    ProfileOfficerUiAction.SetDummyState(ProfileOfficerUiStateDummy())
                )
            }
            UpdateProfileOfficerScreen(
                onNavigateBack = {}
            )
        }
    }
}