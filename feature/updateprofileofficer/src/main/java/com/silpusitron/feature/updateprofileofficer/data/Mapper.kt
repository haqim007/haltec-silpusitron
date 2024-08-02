package com.silpusitron.feature.updateprofileofficer.data

import com.silpusitron.common.util.phoneNumberFormat
import com.silpusitron.shared.form.domain.model.InputTextData
import com.silpusitron.shared.form.domain.model.TextValidationType
import com.silpusitron.feature.updateprofileofficer.data.remote.ProfileOfficerRequest
import com.silpusitron.feature.updateprofileofficer.data.remote.ProfileOfficerResponse
import com.silpusitron.feature.updateprofileofficer.data.remote.SubmitProfileOfficerResponse
import com.silpusitron.feature.updateprofileofficer.domain.FormProfileOfficerInputKey
import com.silpusitron.feature.updateprofileofficer.domain.ProfileOfficerData

internal fun ProfileOfficerResponse.toProfileOfficerData(): ProfileOfficerData {
    val data = this.data
    with(data){
        return ProfileOfficerData(
            id = id,
            userId = userId,
            fingerId = fingerId,
            input = mapOf(
                FormProfileOfficerInputKey.NIP to InputTextData(
                    inputName = FormProfileOfficerInputKey.NIP.toString(),
                    message = null,
                    validations = listOf(TextValidationType.Required),
                    value = nip,
                ),
                FormProfileOfficerInputKey.NIK to InputTextData(
                    inputName = FormProfileOfficerInputKey.NIK.toString(),
                    value = nik,
                    validations = listOf(TextValidationType.Required)
                ),
                FormProfileOfficerInputKey.NAME to InputTextData(
                    inputName = FormProfileOfficerInputKey.NAME.toString(),
                    value = nama,
                    validations = listOf(TextValidationType.Required)
                ),
                FormProfileOfficerInputKey.OPD to InputTextData(
                    inputName = FormProfileOfficerInputKey.OPD.toString(),
                    value = opd,
                    validations = listOf(TextValidationType.Required)
                ),
                FormProfileOfficerInputKey.EMPLOYEE_STATUS to InputTextData(
                    inputName = FormProfileOfficerInputKey.EMPLOYEE_STATUS.toString(),
                    value = statusPegawai,
                    validations = listOf(TextValidationType.Required)
                ),
                FormProfileOfficerInputKey.EMAIL_GOV to InputTextData(
                    inputName = FormProfileOfficerInputKey.EMAIL_GOV.toString(),
                    value = emailGov,
                    validations = listOf(TextValidationType.Required)
                ),
                FormProfileOfficerInputKey.EMAIL to InputTextData(
                    inputName = FormProfileOfficerInputKey.EMAIL.toString(),
                    value = email,
                    validations = listOf(
                        TextValidationType.Required,
                        TextValidationType.Email
                    ),
                ),
                FormProfileOfficerInputKey.PHONE_NUMBER to InputTextData(
                    inputName = FormProfileOfficerInputKey.PHONE_NUMBER.toString(),
                    value = noHp.phoneNumberFormat(),
                    validations = listOf(
                        TextValidationType.Required,
                        TextValidationType.MinLength(11),
                        TextValidationType.MaxLength(15)
                    ),
                    prefix = "+62"
                ),
                FormProfileOfficerInputKey.REGION_NAME to InputTextData(
                    inputName = FormProfileOfficerInputKey.REGION_NAME.toString(),
                    value = namaWilayah,
                    validations = listOf(),
                    enabled = false
                ),
                FormProfileOfficerInputKey.REGION_TYPE to InputTextData(
                    inputName = FormProfileOfficerInputKey.REGION_TYPE.toString(),
                    value = tipeWilayah,
                    validations = listOf(),
                    enabled = false
                ),
                FormProfileOfficerInputKey.DISTRICT to InputTextData(
                    inputName = FormProfileOfficerInputKey.DISTRICT.toString(),
                    value = kecamatan,
                    validations = listOf(),
                    enabled = false
                ), FormProfileOfficerInputKey.SUB_DISTRICT to InputTextData(
                    inputName = FormProfileOfficerInputKey.SUB_DISTRICT.toString(),
                    value = desa,
                    validations = listOf(),
                    enabled = false
                )
            )
        )
    }
}


fun SubmitProfileOfficerResponse.toProfileData(
    profileData: ProfileOfficerData
): ProfileOfficerData = this.errors?.let {
    return ProfileOfficerData(
        id = profileData.id,
        userId = profileData.userId,
        fingerId = profileData.fingerId,
        input = mapOf(
            FormProfileOfficerInputKey.NIP to InputTextData(
                inputName = FormProfileOfficerInputKey.NIP.toString(),
                message = it.nip,
                validations = listOf(TextValidationType.Required),
                value = profileData.input[FormProfileOfficerInputKey.NIP]?.value,
            ),
            FormProfileOfficerInputKey.NIK to InputTextData(
                inputName = FormProfileOfficerInputKey.NIK.toString(),
                message = it.nik,
                value = profileData.input[FormProfileOfficerInputKey.NIK]?.value,
                validations = listOf(TextValidationType.Required)
            ),
            FormProfileOfficerInputKey.NAME to InputTextData(
                inputName = FormProfileOfficerInputKey.NAME.toString(),
                value = profileData.input[FormProfileOfficerInputKey.NAME]?.value,
                message = it.nama,
                validations = listOf(TextValidationType.Required)
            ),
            FormProfileOfficerInputKey.OPD to InputTextData(
                inputName = FormProfileOfficerInputKey.OPD.toString(),
                value = profileData.input[FormProfileOfficerInputKey.EMAIL]?.value,
                message = it.opd,
                validations = listOf(TextValidationType.Required)
            ),
            FormProfileOfficerInputKey.EMPLOYEE_STATUS to InputTextData(
                inputName = FormProfileOfficerInputKey.EMPLOYEE_STATUS.toString(),
                value = profileData.input[FormProfileOfficerInputKey.EMAIL]?.value,
                message = it.statusPegawai,
                validations = listOf(TextValidationType.Required)
            ),
            FormProfileOfficerInputKey.EMAIL_GOV to InputTextData(
                inputName = FormProfileOfficerInputKey.EMAIL_GOV.toString(),
                value = profileData.input[FormProfileOfficerInputKey.EMAIL]?.value,
                message = it.emailGov,
                validations = listOf(TextValidationType.Required)
            ),
            FormProfileOfficerInputKey.EMAIL to InputTextData(
                inputName = FormProfileOfficerInputKey.EMAIL.toString(),
                value = profileData.input[FormProfileOfficerInputKey.EMAIL]?.value,
                message = it.email,
                validations = listOf(
                    TextValidationType.Required,
                    TextValidationType.Email
                ),
            ),
            FormProfileOfficerInputKey.PHONE_NUMBER to InputTextData(
                inputName = FormProfileOfficerInputKey.PHONE_NUMBER.toString(),
                value = profileData.input[FormProfileOfficerInputKey.PHONE_NUMBER]?.value,
                message = it.noHp.phoneNumberFormat(),
                validations = listOf(
                    TextValidationType.Required,
                    TextValidationType.MinLength(11),
                    TextValidationType.MaxLength(15)
                ),
                prefix = "+62"
            ),
            FormProfileOfficerInputKey.REGION_NAME to InputTextData(
                inputName = FormProfileOfficerInputKey.REGION_NAME.toString(),
                value = profileData.input[FormProfileOfficerInputKey.REGION_NAME]?.value,
                validations = listOf(),
                enabled = false
            ),
            FormProfileOfficerInputKey.REGION_TYPE to InputTextData(
                inputName = FormProfileOfficerInputKey.REGION_TYPE.toString(),
                value = profileData.input[FormProfileOfficerInputKey.REGION_TYPE]?.value,
                validations = listOf(),
                enabled = false
            ),
            FormProfileOfficerInputKey.DISTRICT to InputTextData(
                inputName = FormProfileOfficerInputKey.DISTRICT.toString(),
                value = profileData.input[FormProfileOfficerInputKey.DISTRICT]?.value,
                validations = listOf(),
                enabled = false
            ), FormProfileOfficerInputKey.SUB_DISTRICT to InputTextData(
                inputName = FormProfileOfficerInputKey.SUB_DISTRICT.toString(),
                value = profileData.input[FormProfileOfficerInputKey.SUB_DISTRICT]?.value,
                validations = listOf(),
                enabled = false
            )
        )
    )
} ?: profileData


internal fun Map<FormProfileOfficerInputKey, InputTextData<TextValidationType, String>>
        .toProfileRequest(): ProfileOfficerRequest {
    return ProfileOfficerRequest(
        data = ProfileOfficerRequest.Data(
            nik = this[FormProfileOfficerInputKey.NIK]?.value ?: "",
            opd = this[FormProfileOfficerInputKey.OPD]?.value ?: "",
            nama = this[FormProfileOfficerInputKey.NAME]?.value ?: "",
            nip = this[FormProfileOfficerInputKey.NIP]?.value ?: "",
            noHp = this[FormProfileOfficerInputKey.PHONE_NUMBER]?.value ?: "",
            emailGov = this[FormProfileOfficerInputKey.EMAIL_GOV]?.value ?: "",
            email = this[FormProfileOfficerInputKey.EMAIL]?.value ?: "",
            statusPegawai = this[FormProfileOfficerInputKey.EMPLOYEE_STATUS]?.value ?: ""
        )
    )
}