package com.haltec.silpusitron.user.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.haltec.silpusitron.core.domain.model.InputTextData
import com.haltec.silpusitron.core.domain.model.TextValidationType
import com.haltec.silpusitron.core.ui.component.FormDropDown
import com.haltec.silpusitron.core.ui.component.FormTextField
import com.haltec.silpusitron.core.ui.parts.ContainerWithBanner
import com.haltec.silpusitron.core.ui.theme.DisabledInputContainer
import com.haltec.silpusitron.core.ui.theme.SILPUSITRONTheme
import com.haltec.silpusitron.user.R

data class FormProfileUiState(
    val fullname: String = "Stewie Griffin",
    val fullnameInput: InputTextData<TextValidationType, String> = InputTextData(
        inputName = "nama_lengkap",
        value = "",
        validations = listOf()
    ),
    val famCardNumber: String = "0987541234567",
    val famCardNumberInput: InputTextData<TextValidationType, String> = InputTextData(
        inputName = "no_kk",
        value = "",
        validations = listOf()
    ),
    val idNumber: String = "1456789987654",
    val idNumberInput: InputTextData<TextValidationType, String> = InputTextData(
        inputName = "nik",
        value = "",
        validations = listOf()
    ),
    val birthDate: String = "12-12-2003",
    val birthDateInput: InputTextData<TextValidationType, String> = InputTextData(
        inputName = "tanggal_lahir",
        value = "",
        validations = listOf()
    ),
    val gender: String = "",
    val genderInput: InputTextData<TextValidationType, String> = InputTextData(
        inputName = "jenis_kelamin",
        value = "",
        validations = listOf(
            TextValidationType.Required
        ),
        // dummy options
        options = listOf(
            InputTextData.Option(
                key = "JENIS_KELAMIN",
                value = "21",
                label = "Laki-Laki"
            ),
            InputTextData.Option(
                key = "JENIS_KELAMIN",
                value = "22",
                label = "Perempuan"
            )
        )
    ),
    val phoneNumber: String = "",
    val phonenumberInput: InputTextData<TextValidationType, String> = InputTextData(
        inputName = "no_hp",
        value = "",
        validations = listOf(
            TextValidationType.Required,
            TextValidationType.MinLength(11),
            TextValidationType.MaxLength(13)
        ),
    ),
    val district: String = "",
    val districtInput: InputTextData<TextValidationType, String> = InputTextData(
        inputName = "kecamatan_id",
        value = "",
        validations = listOf(
            TextValidationType.Required,
        ),
        // dummy options
        options = listOf(
            InputTextData.Option(
                key = "kecamatan",
                value = "1",
                label = "Kepanjenkidul"
            ),
            InputTextData.Option(
                key = "desa",
                value = "2",
                label = "Sananwetan"
            )
        )
    ),
    val subDistrict: String = "",
    val subDistrictInput: InputTextData<TextValidationType, String> = InputTextData(
        inputName = "kelurahan",
        value = "",
        validations = listOf(
            TextValidationType.Required,
        ),
        // dummy options
        options = listOf(
            InputTextData.Option(
                key = "desa",
                value = "1",
                label = "Bendo"
            ),
            InputTextData.Option(
                key = "desa",
                value = "2",
                label = "Bendogerit"
            )
        )
    ),
    val address: String = "",
    val addressInput: InputTextData<TextValidationType, String> = InputTextData(
        inputName = "alamat",
        value = "",
        validations = listOf(
            TextValidationType.Required,
            TextValidationType.MinLength(11),
            TextValidationType.MaxLength(13)
        ),
    ),
    val noRW: String = "",
    val noRWInput: InputTextData<TextValidationType, String> = InputTextData(
        inputName = "rw",
        value = "",
        validations = listOf(
            TextValidationType.Required,
        ),
    ),
    val noRT: String = "",
    val noRTInput: InputTextData<TextValidationType, String> = InputTextData(
        inputName = "rt",
        value = "",
        validations = listOf(
            TextValidationType.Required,
        ),
    ),
    val birthPlace: String = "",
    val birthPlaceInput: InputTextData<TextValidationType, String> = InputTextData(
        inputName = "tempat_lahir",
        value = "",
        validations = listOf(
            TextValidationType.Required,
            TextValidationType.MinLength(3),
        ),
    ),
    val religion: String = "",
    val religionInput: InputTextData<TextValidationType, String> = InputTextData(
        inputName = "agama_id",
        value = "",
        validations = listOf(
            TextValidationType.Required,
        ),
        // dummy options
        options = listOf(
            InputTextData.Option(
                value = "1",
                label = "Islam",
                key = "agama"
            ),
            InputTextData.Option(
                value = "2",
                label = "Islam",
                key = "agama"
            )
        )
    ),
    val marriageStatus: String = "",
    val marriageStatusInput: InputTextData<TextValidationType, String> = InputTextData(
        inputName = "status_pernikahan",
        value = "",
        validations = listOf(
            TextValidationType.Required,
        ),
        // dummy options
        options = listOf(
            InputTextData.Option(
                value = "1",
                label = "Kawin",
                key = "status_kawin"
            ),
            InputTextData.Option(
                value = "2",
                label = "Kawin Lagi",
                key = "status_kawin"
            )
        )
    ),
    val profession: String = "",
    val professionInput: InputTextData<TextValidationType, String> = InputTextData(
        inputName = "pekerjaan",
        value = "",
        validations = listOf(
            TextValidationType.Required
        ),
        // dummy options
        options = listOf(
            InputTextData.Option(
                value = "23",
                label = "ANGGOTA DPRD KABUPATEN/KOTA",
                key = "PEKERJAAN"
            ),
            InputTextData.Option(
                value = "24",
                label = "AKUNTAN",
                key = "PEKERJAAN"
            )
        )
    ),
    val education: String = "",
    val educationInput: InputTextData<TextValidationType, String> = InputTextData(
        inputName = "pendidikan_id",
        value = "",
        validations = listOf(
            TextValidationType.Required,
        ),
        // dummy options
        options = listOf(
            InputTextData.Option(
                value = "98",
                label = "TIDAK/BELUM SEKOLAH",
                key = "PENDIDIKAN"
            ),
            InputTextData.Option(
                value = "99",
                label = "SD SEKOLAH",
                key = "PENDIDIKAN"
            )
        )
    ),
    val famRelationStatus: String = "",
    val famRelationStatusInput: InputTextData<TextValidationType, String> = InputTextData(
        inputName = "status_hubkel",
        value = "",
        validations = listOf(
            TextValidationType.Required,
        ),
        // dummy options
        options = listOf(
            InputTextData.Option(
                value = "118",
                label = "ANAK",
                key = "HUBUNGAN_KELUARGA"
            ),
            InputTextData.Option(
                value = "119",
                label = "CUCU",
                key = "HUBUNGAN_KELUARGA"
            )
        )
    )


)

@Composable
fun FormProfileScreen(
    modifier: Modifier = Modifier
){

    val state by remember {
        mutableStateOf(FormProfileUiState())
    }

    ContainerWithBanner(
        containerModifier = modifier
            .fillMaxSize(),
        bannerModifier = Modifier
            .height(242.dp),
        sharedModifier = Modifier,
        scrollable = false
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())

        ) {

            FormTextField(
                value = state.fullname,
                onValueChange = {},
                label = "Nama",
                inputData = state.fullnameInput,
                readOnly = true,
                enabled = false,
                modifier = Modifier.fillMaxWidth()
            )

            FormTextField(
                value = state.famCardNumber,
                onValueChange = {},
                label = "No KK",
                inputData = state.famCardNumberInput,
                readOnly = true,
                enabled = false,
                modifier = Modifier.fillMaxWidth()
            )

            FormTextField(
                value = state.idNumber,
                onValueChange = {},
                label = "NIK",
                inputData = state.idNumberInput,
                readOnly = true,
                enabled = false,
                modifier = Modifier.fillMaxWidth()
            )

            FormTextField(
                value = state.birthDate,
                onValueChange = {},
                label = "Tanggal Lahir",
                inputData = state.birthDateInput,
                readOnly = true,
                enabled = false,
                modifier = Modifier.fillMaxWidth()
            )

            FormDropDown(
                label = stringResource(R.string.gender),
                value = state.gender,
                onValueChange = {},
                inputData = state.genderInput
                // TODO: add isLoading & error
            )

            FormTextField(
                value = state.phoneNumber,
                onValueChange = {},
                label = stringResource(R.string.phone_number),
                inputData = state.phonenumberInput,
                // TODO: add isLoading & error
                modifier = Modifier.fillMaxWidth(),
                prefix = {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp))
                            .background(color = DisabledInputContainer)
                            .padding(start = 3.dp, top = 3.dp, bottom = 5.dp)
                            .height(20.dp)
                            .width(35.dp)
                    ){
                        Text(text = stringResource(R.string.plus_62))
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            FormDropDown(
                label = stringResource(id = R.string.district),
                value = state.district,
                onValueChange = {},
                inputData = state.districtInput
                // TODO: add isLoading & error
            )

            FormDropDown(
                label = stringResource(id = R.string.sub_district),
                value = state.subDistrict,
                onValueChange = {},
                inputData = state.subDistrictInput
                // TODO: add isLoading & error
            )

            FormTextField(
                value = state.address,
                onValueChange = {},
                label = stringResource(R.string.address),
                inputData = state.addressInput,
                // TODO: add isLoading & error
                modifier = Modifier.fillMaxWidth(),
                singleLine = false
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FormTextField(
                    value = state.noRT,
                    onValueChange = {},
                    label = stringResource(R.string.neighborhood),
                    inputData = state.noRTInput,
                    // TODO: add isLoading & error
                    modifier = modifier.weight(1f),
                    singleLine = true
                )
                FormTextField(
                    value = state.noRW,
                    onValueChange = {},
                    label = stringResource(R.string.citizenhood),
                    inputData = state.noRWInput,
                    // TODO: add isLoading & error
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
            }

            FormTextField(
                value = state.birthPlace,
                onValueChange = {},
                label = stringResource(R.string.birth_place),
                inputData = state.birthPlaceInput,
                // TODO: add isLoading & error
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            FormDropDown(
                label = stringResource(id = R.string.religion),
                value = state.religion,
                onValueChange = {},
                inputData = state.religionInput
                // TODO: add isLoading & error
            )

            FormDropDown(
                label = stringResource(id = R.string.marriage_status),
                value = state.marriageStatus,
                onValueChange = {},
                inputData = state. marriageStatusInput
                // TODO: add isLoading & error
            )

            FormDropDown(
                label = stringResource(id = R.string.profession),
                value = state.profession,
                onValueChange = {},
                inputData = state.professionInput
                // TODO: add isLoading & error
            )

            FormDropDown(
                label = stringResource(id = R.string.education),
                value = state.education,
                onValueChange = {},
                inputData = state.educationInput
                // TODO: add isLoading & error
            )

            FormDropDown(
                label = stringResource(id = R.string.family_relation_status),
                value = state.famRelationStatus,
                onValueChange = {},
                inputData = state.famRelationStatusInput
                // TODO: add isLoading & error
            )

            Button(
                modifier = Modifier
                    .padding(top = 50.dp, bottom = 6.dp)
                    .wrapContentWidth()
                    .align(Alignment.CenterHorizontally)
                ,
                onClick = {  },
                colors = ButtonDefaults.buttonColors().copy(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                shape = RoundedCornerShape(5.dp),
                enabled = true //state.value.enableLogin && !isLoading
            ) {
                Text(
                    text = stringResource(R.string.save),
                    fontWeight = FontWeight.Bold
                )
            }


        }
    }
}

@Preview(showBackground = true)
@Composable
fun FormProfileScreenPreview(){
    SILPUSITRONTheme {
        FormProfileScreen()
    }
}
