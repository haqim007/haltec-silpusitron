package com.haltec.silpusitron.user.profile.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.animateScrollBy
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.haltec.silpusitron.common.di.commonModule
import com.haltec.silpusitron.core.ui.component.InputLabel
import com.haltec.silpusitron.core.ui.component.LottieLoader
import com.haltec.silpusitron.core.ui.parts.ContainerWithBanner
import com.haltec.silpusitron.core.ui.parts.ErrorView
import com.haltec.silpusitron.core.ui.parts.LoadingView
import com.haltec.silpusitron.core.ui.parts.SubmitSuccessView
import com.haltec.silpusitron.core.ui.theme.DisabledInputContainer
import com.haltec.silpusitron.core.ui.theme.SILPUSITRONTheme
import com.haltec.silpusitron.core.ui.util.KoinPreviewWrapper
import com.haltec.silpusitron.data.di.dataModule
import com.haltec.silpusitron.data.mechanism.Resource
import com.haltec.silpusitron.shared.form.domain.model.isRequired
import com.haltec.silpusitron.shared.form.domain.model.valueOrEmpty
import com.haltec.silpusitron.shared.form.ui.components.FormDropDown
import com.haltec.silpusitron.shared.form.ui.components.FormTextField
import com.haltec.silpusitron.shared.formprofile.data.dummy.formProfileInputDummy
import com.haltec.silpusitron.shared.formprofile.domain.model.FormProfileInputKey
import com.haltec.silpusitron.shared.formprofile.domain.model.ProfileDataDummy
import com.haltec.silpusitron.user.profile.di.profileModule
import io.ktor.http.HttpStatusCode
import org.koin.androidx.compose.koinViewModel
import com.haltec.silpusitron.core.ui.R as CoreR
import com.haltec.silpusitron.shared.formprofile.R as FormProfileR


@Composable
fun ProfileDataScreen(
    modifier: Modifier = Modifier,
    viewModel: ProfileDataViewModel = koinViewModel(),
    onTokenExpired: () -> Unit,
    onComplete: () -> Unit
){
    val state by viewModel.state.collectAsState()
    val action = { action: ProfileDataUiAction -> viewModel.doAction(action) }

    LaunchedEffect(key1 = Unit) {
        action(ProfileDataUiAction.GetProfileData)
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

            when (state.profileData) {
                is Resource.Success -> {
                    ProfileDataForm(
                        modifier = Modifier,
                        state = state,
                        action = action
                    )
                }

                is Resource.Error -> {
                    ErrorView(
                        message = state.profileData.message,
                        onTryAgain = { action(ProfileDataUiAction.GetProfileData) }
                    )
                }

                else -> {
                    LoadingView()
                }
            }

        }

        when(state.submitResult){
            is Resource.Error -> {
                if (state.submitResult.httpCode == HttpStatusCode.Unauthorized.value){
                    onTokenExpired()
                }
                else{
                    SubmitErrorView(
                        state.submitResult.message,
                        onTryAgain = {
                            action(ProfileDataUiAction.ResetSubmitResult)
                        }
                    )
                }
            }
            is Resource.Idle -> Unit
            is Resource.Loading -> {
                SubmitLoadingView()
            }
            is Resource.Success ->{
                SubmitSuccessView(onComplete = onComplete)
            }
        }

    }
}

@Composable
private fun SubmitErrorView(
    message: String?,
    onTryAgain: () -> Unit
) {
    Box(
        modifier = Modifier
            .wrapContentHeight()
            .width(250.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(
                MaterialTheme.colorScheme.background
            )
            .border(
                BorderStroke(
                    width = 2.dp,
                    color = Color.Red.copy(
                        alpha = 0.5f
                    )
                ),
                RoundedCornerShape(10.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LottieLoader(
                jsonRaw = CoreR.raw.lottie_error,
                modifier = Modifier
                    .size(200.dp),
                iterations = 10
            )
            Text(
                text = message ?: stringResource(CoreR.string.unknown_error),
                style = TextStyle.Default.copy(
                    color = Color.Red,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                ),
                textAlign = TextAlign.Center
            )
            Button(
                modifier = Modifier.padding(top = 10.dp),
                onClick = {
                    onTryAgain()
                },
                shape = RoundedCornerShape(5.dp),
            ) {
                Text(
                    stringResource(CoreR.string.try_again),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun SubmitLoadingView() {
    Box(
        modifier = Modifier
            .wrapContentSize()
            .background(DisabledInputContainer),
        contentAlignment = Alignment.Center
    ) {
        LoadingView(
            loader = {
                LottieLoader(
                    jsonRaw = CoreR.raw.lottie_loading_doc,
                    modifier = Modifier
                        .size(250.dp)
                        .padding(bottom = 1.dp)
                )
            },
            messageStyle = TextStyle.Default.copy(
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        )
    }
}



@Composable
fun ProfileDataForm(
    modifier: Modifier = Modifier,
    state: ProfileDataUiState,
    action: (action: ProfileDataUiAction) -> Unit
){

    val fullNameInput = state.inputs[FormProfileInputKey.FULL_NAME]
    val famCardNumberInput = state.inputs[FormProfileInputKey.FAM_CARD_NUMBER]
    val idNumberInput = state.inputs[FormProfileInputKey.ID_NUMBER]
    val birthDateInput = state.inputs[FormProfileInputKey.BIRTH_DATE]
    val districtInput = state.inputs[FormProfileInputKey.DISTRICT]
    val subDistrictInput = state.inputs[FormProfileInputKey.SUB_DISTRICT]
    val motherNameInput = state.inputs[FormProfileInputKey.MOTHER_NAME]
    val addressInput = state.inputs[FormProfileInputKey.ADDRESS]
    val birthPlaceInput = state.inputs[FormProfileInputKey.BIRTH_PLACE]
    val bloodTypeInput = state.inputs[FormProfileInputKey.BLOOD_TYPE]
    val educationInput = state.inputs[FormProfileInputKey.EDUCATION]
    val famRelationInput = state.inputs[FormProfileInputKey.FAMILY_RELATION]
    val fatherNameInput = state.inputs[FormProfileInputKey.FATHER_NAME]
    val genderInput = state.inputs[FormProfileInputKey.GENDER]
    val marriageStatusInput = state.inputs[FormProfileInputKey.MARRIAGE_STATUS]
    val phoneNumberInput = state.inputs[FormProfileInputKey.PHONE_NUMBER]
    val professionInput = state.inputs[FormProfileInputKey.PROFESSION]
    val religionInput = state.inputs[FormProfileInputKey.RELIGION]
    val rtInput = state.inputs[FormProfileInputKey.RT]
    val rwInput = state.inputs[FormProfileInputKey.RW]

    val scrollState = rememberScrollState()

    // Load all options after composition completed
    LaunchedEffect(key1 = Unit) {
        action(ProfileDataUiAction.GetBloodTypeOptions)
        action(ProfileDataUiAction.GetEducationOptions)
        action(ProfileDataUiAction.GetFamRelationStatusOptions)
        action(ProfileDataUiAction.GetGenderOptions)
        action(ProfileDataUiAction.GetMarriageStatusOptions)
        action(ProfileDataUiAction.GetProfessionOptions)
        action(ProfileDataUiAction.GetReligionOptions)
        action(ProfileDataUiAction.GetDistrictOptions)
    }

    // load subdistricts when districtInput changes
    LaunchedEffect(key1 = districtInput) {
        action(ProfileDataUiAction.GetSubDistrictOptions)
    }

    // Auto scroll whenever firstErrorInputKey changes
    LaunchedEffect(key1 = state.firstErrorInputKey) {
        state.inputsCoordinateY[state.firstErrorInputKey]?.let {
            scrollState.animateScrollBy(it)
            action(ProfileDataUiAction.ResetFirstErrorInputKey)
        }
    }
    
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
            .verticalScroll(scrollState)

    ) {

        if(fullNameInput != null) {
            FormTextField(
                value = fullNameInput.valueOrEmpty(),
                onValueChange = {},
                inputLabel = stringResource(FormProfileR.string.name),
                label = {
                    InputLabel(
                        label = stringResource(FormProfileR.string.name),
                        isRequired = fullNameInput.isRequired()
                    )
                },
                inputData = fullNameInput,
                readOnly = true,
                enabled = false,
                modifier = Modifier.fillMaxWidth()
            )
        }

        famCardNumberInput?.let {
            FormTextField(
                value = it.valueOrEmpty(),
                onValueChange = {},
                inputLabel = stringResource(FormProfileR.string.family_card_number),
                label = {
                    InputLabel(
                        label = stringResource(FormProfileR.string.family_card_number),
                        isRequired = it.isRequired()
                    )
                },
                inputData = it,
                readOnly = true,
                enabled = false,
                modifier = Modifier.fillMaxWidth()
            )
        }

        idNumberInput?.let {
            FormTextField(
                value = it.valueOrEmpty(),
                onValueChange = {},
                inputLabel = stringResource(FormProfileR.string.id_number),
                label = {
                    InputLabel(
                        label = stringResource(FormProfileR.string.id_number),
                        isRequired = it.isRequired()
                    )
                },
                inputData = it,
                readOnly = true,
                enabled = false,
                modifier = Modifier.fillMaxWidth()
            )
        }

        birthDateInput?.let {
            FormTextField(
                value = it.valueOrEmpty(),
                onValueChange = {},
                inputLabel = stringResource(FormProfileR.string.birth_date),
                label = {
                    InputLabel(
                        label = stringResource(FormProfileR.string.birth_date),
                        isRequired = it.isRequired()
                    )
                },
                inputData = it,
                readOnly = true,
                enabled = false,
                modifier = Modifier.fillMaxWidth()
            )
        }

        genderInput?.let {
            FormDropDown(
                inputLabel = stringResource(FormProfileR.string.gender),
                label = {
                    InputLabel(
                        label = stringResource(FormProfileR.string.gender),
                        isRequired = genderInput.isRequired()
                    )
                },
                value = genderInput.valueOrEmpty(),
                onValueChange = {
                    action(
                        ProfileDataUiAction
                            .SetInput(
                                FormProfileInputKey.fromString(genderInput.inputName),
                                it.value
                            )
                    )
                },
                inputData = genderInput,
                isLoading = state.genderOptions is Resource.Loading
            )
        }

        phoneNumberInput?.let {
            val inputName = FormProfileInputKey.fromString(it.inputName)
            FormTextField(
                value = it.valueOrEmpty(),
                onValueChange = { newValue ->
                    action(
                        ProfileDataUiAction
                            .SetInput(
                                inputName,
                                newValue
                            )
                    )
                },
                inputLabel = stringResource(FormProfileR.string.phone_number),
                label = {
                    InputLabel(
                        label = stringResource(FormProfileR.string.phone_number),
                        isRequired = it.isRequired()
                    )
                },
                inputData = it,
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned { coordinates ->
                        action(
                            ProfileDataUiAction.SaveInputViewCoordinateY(
                                inputName,
                                coordinates.positionInRoot().y
                            )
                        )
                    },
                prefix = {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .clip(RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp))
                            .background(color = DisabledInputContainer)
                            .padding(start = 3.dp, top = 1.dp, bottom = 1.dp, end = 5.dp)
                    ){
                        Text(text = stringResource(FormProfileR.string.plus_62), fontSize = 14.sp)
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                singleLine = true
            )
        }

        districtInput?.let {
            FormDropDown(
                inputLabel = stringResource(FormProfileR.string.district),
                label = {
                    InputLabel(
                        label = stringResource(FormProfileR.string.district),
                        isRequired = it.isRequired()
                    )
                },
                value = it.valueOrEmpty(),
                onValueChange = { newValue ->
                    action(
                        ProfileDataUiAction.SetInput(
                            FormProfileInputKey.DISTRICT,
                            newValue.value
                        )
                    )
                },
                inputData = it,
                isLoading = state.districtOptions is Resource.Loading,
                loadNetworkError = state.districtOptions is Resource.Error,
                reloadNetwork = {
                    action(ProfileDataUiAction.GetDistrictOptions)
                }
            )
        }

        subDistrictInput?.let {
            FormDropDown(
                inputLabel = stringResource(FormProfileR.string.sub_district),
                label = {
                    InputLabel(
                        label = stringResource(FormProfileR.string.sub_district),
                        isRequired = subDistrictInput.isRequired()
                    )
                },
                value = subDistrictInput.valueOrEmpty(),
                onValueChange = {
                    action(
                        ProfileDataUiAction.SetInput(
                            FormProfileInputKey.fromString(subDistrictInput.inputName),
                            it.value
                        )
                    )
                },
                inputData = subDistrictInput,
                isLoading = state.subDistrictOptions is Resource.Loading,
                enabled = districtInput?.options?.isNotEmpty() == true &&
                        districtInput.value?.isNotEmpty() == true,
                loadNetworkError = state.subDistrictOptions is Resource.Error,
                reloadNetwork = {
                    action(ProfileDataUiAction.GetSubDistrictOptions)
                }
            )
        }

        addressInput?.let {
            val inputName = FormProfileInputKey.fromString(addressInput.inputName)
            FormTextField(
                value = addressInput.valueOrEmpty(),
                onValueChange = {
                    action(
                        ProfileDataUiAction
                            .SetInput(
                                inputName,
                                it
                            )
                    )
                },
                inputLabel = stringResource(FormProfileR.string.address),
                label = {
                    InputLabel(
                        label = stringResource(FormProfileR.string.address),
                        isRequired = addressInput.isRequired()
                    )
                },
                inputData = addressInput,
                // TODO: add isLoading & error
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned {
                        action(
                            ProfileDataUiAction.SaveInputViewCoordinateY(
                                inputName,
                                it.positionInRoot().y
                            )
                        )
                    },
                singleLine = false,
            )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            rtInput?.let {
                val inputName = FormProfileInputKey.fromString(rtInput.inputName)
                FormTextField(
                    value = rtInput.valueOrEmpty(),
                    onValueChange = {
                        action(
                            ProfileDataUiAction
                                .SetInput(
                                    inputName,
                                    it
                                )
                        )
                    },
                    inputLabel = stringResource(FormProfileR.string.neighborhood),
                    label = {
                        InputLabel(
                            label = stringResource(FormProfileR.string.neighborhood),
                            isRequired = rtInput.isRequired()
                        )
                    },
                    inputData = rtInput,
                    // TODO: add isLoading & error
                    modifier = modifier
                        .weight(1f)
                        .onGloballyPositioned {
                            action(
                                ProfileDataUiAction.SaveInputViewCoordinateY(
                                    inputName,
                                    it.positionInRoot().y
                                )
                            )
                        },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    )
                )
            }
            rwInput?.let {
                val inputName = FormProfileInputKey.fromString(rwInput.inputName)
                FormTextField(
                    value = rwInput.valueOrEmpty(),
                    onValueChange = {
                        action(
                            ProfileDataUiAction
                                .SetInput(
                                    inputName,
                                    it
                                )
                        )
                    },
                    inputLabel = stringResource(FormProfileR.string.citizenhood),
                    label = {
                        InputLabel(
                            label = stringResource(FormProfileR.string.citizenhood),
                            isRequired = rwInput.isRequired()
                        )
                    },
                    inputData = rwInput,
                    modifier = Modifier
                        .weight(1f)
                        .onGloballyPositioned {
                            action(
                                ProfileDataUiAction.SaveInputViewCoordinateY(
                                    inputName,
                                    it.positionInRoot().y
                                )
                            )
                        },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    )
                )
            }
        }

        birthPlaceInput?.let {
            val inputName = FormProfileInputKey.fromString(birthPlaceInput.inputName)
            FormTextField(
                value = birthPlaceInput.valueOrEmpty(),
                onValueChange = {
                    action(
                        ProfileDataUiAction
                            .SetInput(
                                inputName,
                                it
                            )
                    )
                },
                inputLabel = stringResource(FormProfileR.string.birth_place),
                label = {
                    InputLabel(
                        label = stringResource(FormProfileR.string.birth_place),
                        isRequired = birthPlaceInput.isRequired()
                    )
                },
                inputData = birthPlaceInput,
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned {
                        action(
                            ProfileDataUiAction.SaveInputViewCoordinateY(
                                inputName,
                                it.positionInRoot().y
                            )
                        )
                    },
                singleLine = true
            )
        }

        religionInput?.let {
            FormDropDown(
                inputLabel = stringResource(FormProfileR.string.religion),
                label = {
                    InputLabel(
                        label = stringResource(FormProfileR.string.religion),
                        isRequired = religionInput.isRequired()
                    )
                },
                value = religionInput.valueOrEmpty(),
                onValueChange = {
                    action(
                        ProfileDataUiAction
                            .SetInput(
                                FormProfileInputKey.fromString(religionInput.inputName),
                                it.value
                            )
                    )
                },
                inputData = religionInput,
                isLoading = state.religionOptions is Resource.Loading
            )
        }

        marriageStatusInput?.let {
            FormDropDown(
                inputLabel = stringResource(FormProfileR.string.marriage_status),
                label = {
                    InputLabel(
                        label = stringResource(FormProfileR.string.marriage_status),
                        isRequired = marriageStatusInput.isRequired()
                    )
                },
                value = marriageStatusInput.valueOrEmpty(),
                onValueChange = {
                    action(
                        ProfileDataUiAction
                            .SetInput(
                                FormProfileInputKey.fromString(marriageStatusInput.inputName),
                                it.value
                            )
                    )
                },
                inputData = marriageStatusInput,
                isLoading = state.marriageStatusOptions is Resource.Loading
            )
        }

        professionInput?.let {
            FormDropDown(
                inputLabel = stringResource(FormProfileR.string.profession),
                label = {
                    InputLabel(
                        label = stringResource(FormProfileR.string.profession),
                        isRequired = professionInput.isRequired()
                    )
                },
                value = professionInput.valueOrEmpty(),
                onValueChange = {
                    action(
                        ProfileDataUiAction
                            .SetInput(
                                FormProfileInputKey.fromString(professionInput.inputName),
                                it.value
                            )
                    )
                },
                inputData = professionInput,
                isLoading = state.professionOptions is Resource.Loading
            )
        }

        educationInput?.let {
            FormDropDown(
                inputLabel = stringResource(FormProfileR.string.education),
                label = {
                    InputLabel(
                        label = stringResource(FormProfileR.string.education),
                        isRequired = educationInput.isRequired()
                    )
                },
                value = educationInput.valueOrEmpty(),
                onValueChange = {
                    action(
                        ProfileDataUiAction
                            .SetInput(
                                FormProfileInputKey.fromString(educationInput.inputName),
                                it.value
                            )
                    )
                },
                inputData = educationInput,
                isLoading = state.educationOptions is Resource.Loading
            )
        }

        famRelationInput?.let {
            FormDropDown(
                inputLabel = stringResource(FormProfileR.string.family_relation_status),
                label = {
                    InputLabel(
                        label = stringResource(FormProfileR.string.family_relation_status),
                        isRequired = famRelationInput.isRequired()
                    )
                },
                value = famRelationInput.valueOrEmpty(),
                onValueChange = {
                    action(
                        ProfileDataUiAction
                            .SetInput(
                                FormProfileInputKey.fromString(famRelationInput.inputName),
                                it.value
                            )
                    )
                },
                inputData = famRelationInput,
                isLoading = state.famRelationStatusOptions is Resource.Loading
            )
        }

        bloodTypeInput?.let {
            FormDropDown(
                inputLabel = stringResource(FormProfileR.string.blood_type),
                label = {
                    InputLabel(
                        label = stringResource(FormProfileR.string.blood_type),
                        isRequired = bloodTypeInput.isRequired()
                    )
                },
                value = bloodTypeInput.valueOrEmpty(),
                onValueChange = {
                    action(
                        ProfileDataUiAction
                            .SetInput(
                                FormProfileInputKey.fromString(bloodTypeInput.inputName),
                                it.value
                            )
                    )
                },
                inputData = bloodTypeInput,
                isLoading = state.bloodTypeOptions is Resource.Loading
            )
        }

        fatherNameInput?.let {
            val inputName = FormProfileInputKey.fromString(fatherNameInput.inputName)
            FormTextField(
                value = fatherNameInput.valueOrEmpty(),
                onValueChange = {
                    action(
                        ProfileDataUiAction
                            .SetInput(
                                inputName,
                                it
                            )
                    )
                },
                inputLabel = stringResource(FormProfileR.string.father_name),
                label = {
                    InputLabel(
                        label = stringResource(FormProfileR.string.father_name),
                        isRequired = fatherNameInput.isRequired()
                    )
                },
                inputData = fatherNameInput,
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned {
                        action(
                            ProfileDataUiAction.SaveInputViewCoordinateY(
                                inputName,
                                it.positionInRoot().y
                            )
                        )
                    },
                singleLine = true
            )
        }

        motherNameInput?.let {
            val inputName = FormProfileInputKey.fromString(motherNameInput.inputName)
            FormTextField(
                value = motherNameInput.valueOrEmpty(),
                onValueChange = {
                    action(
                        ProfileDataUiAction
                            .SetInput(
                                inputName,
                                it
                            )
                    )
                },
                inputLabel = stringResource(FormProfileR.string.mother_name),
                label = {
                    InputLabel(
                        label = stringResource(FormProfileR.string.mother_name),
                        isRequired = motherNameInput.isRequired()
                    )
                },
                inputData = motherNameInput,
                // TODO: add isLoading & error
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned {
                        action(
                            ProfileDataUiAction.SaveInputViewCoordinateY(
                                inputName,
                                it.positionInRoot().y
                            )
                        )
                    },
                singleLine = true,
            )
        }

        Button(
            modifier = Modifier
                .padding(top = 50.dp, bottom = 6.dp)
                .wrapContentWidth()
                .align(Alignment.CenterHorizontally)
            ,
            onClick = { action(ProfileDataUiAction.Submit) },
            colors = ButtonDefaults.buttonColors().copy(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            shape = RoundedCornerShape(5.dp),
            enabled = true //state.value.enableLogin && !isLoading
        ) {
            Text(
                text = stringResource(CoreR.string.save),
                fontWeight = FontWeight.Bold
            )
        }


    }
}

private val formProfileStateDummy = ProfileDataUiState(
    inputs = formProfileInputDummy
)

@Preview(showBackground = true, name = "Loading")
@Composable
fun FormProfileScreenPreview(){

    KoinPreviewWrapper(
        modules = listOf(commonModule, dataModule, profileModule)
    ){
        SILPUSITRONTheme {
            ProfileDataScreen(
                onComplete = {}, 
                onTokenExpired = {},
            )
        }
    }
}

@Preview(showBackground = true, name = "Error")
@Composable
fun FormProfileScreenPreviewError(){

    KoinPreviewWrapper(
        modules = listOf(commonModule, dataModule, profileModule)
    ){
        val viewModel: ProfileDataViewModel = koinViewModel()
        LaunchedEffect(key1 = Unit) {
            viewModel.doAction(
                ProfileDataUiAction.SetDummyState(
                    formProfileStateDummy.copy(
                        profileData = Resource.Error(
                            message = "Error occured here but it is just dummy"
                        )
                    )
                )
            )
        }
        SILPUSITRONTheme {
            ProfileDataScreen(
                viewModel = viewModel,
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
        modules = listOf(commonModule, dataModule, profileModule)
    ){
        val viewModel: ProfileDataViewModel = koinViewModel()
        LaunchedEffect(key1 = Unit) {
            viewModel.doAction(
                ProfileDataUiAction.SetDummyState(
                    formProfileStateDummy.copy(
                        profileData = Resource.Success(
                            data = ProfileDataDummy
                        ),
                    )
                )
            )
        }
        SILPUSITRONTheme {
            ProfileDataScreen(
                viewModel = viewModel,
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
        modules = listOf(commonModule, dataModule, profileModule)
    ){
        SILPUSITRONTheme {
            val viewModel: ProfileDataViewModel = koinViewModel()
            LaunchedEffect(key1 = Unit) {
                viewModel.doAction(ProfileDataUiAction.SetDummyState(
                    formProfileStateDummy.copy(
                        profileData = Resource.Success(
                            data = ProfileDataDummy
                        ),
                        submitResult = Resource.Success(ProfileDataDummy)
                    )
                ))
            }
            ProfileDataScreen(
                viewModel = viewModel,
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
        modules = listOf(commonModule, dataModule, profileModule)
    ){
        SILPUSITRONTheme {
            val viewModel: ProfileDataViewModel = koinViewModel()
            LaunchedEffect(key1 = Unit) {
                viewModel.doAction(ProfileDataUiAction.SetDummyState(
                    formProfileStateDummy.copy(
                        profileData = Resource.Success(
                            data = ProfileDataDummy
                        ),
                        submitResult = Resource.Loading()
                    )
                ))
            }
            ProfileDataScreen(
                viewModel = viewModel,
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
        modules = listOf(commonModule, dataModule, profileModule)
    ){
        SILPUSITRONTheme {
            val viewModel: ProfileDataViewModel = koinViewModel()
            LaunchedEffect(key1 = Unit) {
                viewModel.doAction(ProfileDataUiAction.SetDummyState(
                    formProfileStateDummy.copy(
                        profileData = Resource.Success(
                            data = ProfileDataDummy
                        ),
                        submitResult = Resource.Error(message = "Error disini")
                    )
                ))
            }
            ProfileDataScreen(
                viewModel = viewModel,
//                state = formProfileStateDummy.copy(
//                    profileData = Resource.Success(
//                        data = ProfileDataDummy
//                    ),
//                    submitResult = Resource.Error(message = "Error disini")
//                ),
//                action = {action -> },
                onComplete = {}, 
                onTokenExpired = {}
            )
        }

    }

}
