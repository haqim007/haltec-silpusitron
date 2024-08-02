package com.silpusitron.feature.confirmprofilecitizen.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SaveAs
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.silpusitron.common.di.commonModule
import com.haltec.silpusitron.core.ui.parts.ContainerWithBanner
import com.haltec.silpusitron.core.ui.theme.SILPUSITRONTheme
import com.haltec.silpusitron.core.ui.util.KoinPreviewWrapper
import com.silpusitron.data.di.dataModule
import com.silpusitron.data.mechanism.Resource
import com.silpusitron.shared.formprofile.domain.model.ProfileDataDummy
import com.silpusitron.shared.formprofile.ui.FormProfileScreen
import com.silpusitron.shared.formprofile.ui.FormProfileUiAction
import com.silpusitron.shared.formprofile.ui.FormProfileUiState
import com.silpusitron.shared.formprofile.ui.FormProfileViewModel
import com.silpusitron.feature.confirmprofilecitizen.di.confirmProfileCitizenModule
import org.koin.androidx.compose.koinViewModel
import com.haltec.silpusitron.core.ui.R as CoreR


@Composable
fun ConfirmProfileCitizenScreen(
    modifier: Modifier = Modifier,
    formProfileViewModel: FormProfileViewModel = koinViewModel(),
    onTokenExpired: () -> Unit,
    onComplete: () -> Unit
){
    val formProfileState by formProfileViewModel.state.collectAsState()

    var hasLoadForm by remember {
        mutableStateOf(false)
    }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ){

        ContainerWithBanner(
            modifier = modifier
                .fillMaxSize(),
            bannerModifier = Modifier
                .height(242.dp),
            sharedModifier = Modifier,
            scrollable = false
        ) {

            FormProfileScreen(
                hasLoadForm = hasLoadForm,
                setHasLoadForm = {
                    hasLoadForm = true
                },
                state = formProfileState,
                action = { action -> formProfileViewModel.doAction(action) },
                onSuccessSubmit = onComplete,
                onTokenExpired = onTokenExpired,
                additionalContent = {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                        Button(
                            modifier = Modifier,
                            onClick = { formProfileViewModel.doAction(FormProfileUiAction.Submit) },
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
            )

        }
    }
}

@Preview(showBackground = true, name = "Loading")
@Composable
fun FormProfileScreenPreview(){

    KoinPreviewWrapper(
        modules = listOf(commonModule, dataModule, confirmProfileCitizenModule)
    ){
        SILPUSITRONTheme {
            ConfirmProfileCitizenScreen(
                onComplete = {},
                onTokenExpired = {}
            )
        }
    }
}

@Preview(showBackground = true, name = "Error")
@Composable
fun FormProfileScreenPreviewError(){

    KoinPreviewWrapper(
        modules = listOf(commonModule, dataModule, confirmProfileCitizenModule)
    ){
        val viewModel: FormProfileViewModel = koinViewModel()
        LaunchedEffect(key1 = Unit) {
            viewModel.doAction(
                FormProfileUiAction.SetDummyState(
                    state = FormProfileUiState(
                        profileData = Resource.Error(
                            message = "Error occured here but it is just dummy"
                        )
                    )
                )
            )
        }
        SILPUSITRONTheme {
            ConfirmProfileCitizenScreen(
                formProfileViewModel = viewModel,
                onComplete = {},
                onTokenExpired = {}
            )
        }
    }

}

@Preview(showBackground = true, name = "Success")
@Composable
fun FormProfileScreenPreviewSuccess(){

    KoinPreviewWrapper(
        modules = listOf(commonModule, dataModule, confirmProfileCitizenModule)
    ){
        val viewModel: FormProfileViewModel = koinViewModel()
        LaunchedEffect(key1 = Unit) {
            viewModel.doAction(
                FormProfileUiAction.SetDummyState(
                    state = FormProfileUiState(
                        profileData = Resource.Error(
                            message = "Error occured here but it is just dummy"
                        )
                    )
                )
            )
        }
        SILPUSITRONTheme {
            ConfirmProfileCitizenScreen(
                formProfileViewModel = viewModel,
                onComplete = {},
                onTokenExpired = {}
            )
        }

    }

}

@Preview(showBackground = true, name = "Submit Success")
@Composable
fun FormProfileScreenPreviewSubmitSuccess(){

    KoinPreviewWrapper(
        modules = listOf(commonModule, dataModule, confirmProfileCitizenModule)
    ){
        SILPUSITRONTheme {
            val viewModel: FormProfileViewModel = koinViewModel()
            LaunchedEffect(key1 = Unit) {
                viewModel.doAction(
                    FormProfileUiAction.SetDummyState(
                        state = FormProfileUiState(
                            profileData = Resource.Success(
                                data = ProfileDataDummy
                            )
                        )
                    )
                )
            }
            ConfirmProfileCitizenScreen(
                formProfileViewModel = viewModel,
                onComplete = {},
                onTokenExpired = {}
            )
        }

    }

}

@Preview(showBackground = true, name = "Submit Loading")
@Composable
fun FormProfileScreenPreviewSubmitLoading(){

    KoinPreviewWrapper(
        modules = listOf(commonModule, dataModule, confirmProfileCitizenModule)
    ){
        SILPUSITRONTheme {
            val viewModel: FormProfileViewModel = koinViewModel()
            LaunchedEffect(key1 = Unit) {
                viewModel.doAction(
                    FormProfileUiAction.SetDummyState(
                        state = FormProfileUiState(
                            profileData = Resource.Success(
                                data = ProfileDataDummy
                            ),
                            submitResult = Resource.Loading()
                        )
                    )
                )
            }

            ConfirmProfileCitizenScreen(
                formProfileViewModel = viewModel,
                onComplete = {},
                onTokenExpired = {}
            )
        }

    }

}

@Preview(showBackground = true, name = "Submit Error")
@Composable
fun FormProfileScreenPreviewSubmitError(){

    KoinPreviewWrapper(
        modules = listOf(commonModule, dataModule, confirmProfileCitizenModule)
    ){
        SILPUSITRONTheme {
            val viewModel: FormProfileViewModel = koinViewModel()
            LaunchedEffect(key1 = Unit) {
                viewModel.doAction(
                    FormProfileUiAction.SetDummyState(
                        state = FormProfileUiState(
                            profileData = Resource.Success(
                                data = ProfileDataDummy
                            ),
                            submitResult = Resource.Error(message = "Error disini")
                        )
                    )
                )
            }
            ConfirmProfileCitizenScreen(
                formProfileViewModel = viewModel,
                onComplete = {},
                onTokenExpired = {}
            )
        }

    }

}
