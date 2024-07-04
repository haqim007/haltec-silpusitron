package com.haltec.silpusitron.user.profile.data

import com.haltec.silpusitron.core.domain.model.InputTextData
import com.haltec.silpusitron.core.domain.model.TextValidationType
import com.haltec.silpusitron.user.profile.data.remote.request.ProfileRequest
import com.haltec.silpusitron.user.profile.data.remote.response.ProfileResponse
import com.haltec.silpusitron.user.profile.data.remote.response.SubmitProfileResponse
import com.haltec.silpusitron.user.profile.domain.model.FormProfileInputKey
import com.haltec.silpusitron.user.profile.domain.model.ProfileData

fun SubmitProfileResponse.toProfileData(
    profileData: ProfileData
): ProfileData = this.errors?.let {
    return ProfileData(
        id = profileData.id,
        active = profileData.active,
        userId = profileData.userId,
        input = mapOf(
            FormProfileInputKey.SUB_DISTRICT to InputTextData(
                inputName = FormProfileInputKey.SUB_DISTRICT.toString(),
                message = errors.desaId,
                validations = listOf(TextValidationType.Required),
                value = profileData.input[FormProfileInputKey.SUB_DISTRICT]?.value ?: "",
            ),
            FormProfileInputKey.RT to InputTextData(
                inputName = FormProfileInputKey.RT.toString(),
                message = errors.rt,
                validations = listOf(TextValidationType.Required),
                value = profileData.input[FormProfileInputKey.RT]?.value ?: ""
            ),
            FormProfileInputKey.PHONE_NUMBER to InputTextData(
                inputName = FormProfileInputKey.PHONE_NUMBER.toString(),
                message = errors.noHp,
                validations = listOf(
                    TextValidationType.Required,
                    TextValidationType.MinLength(9),
                    TextValidationType.MaxLength(15)
                ),
                value = profileData.input[FormProfileInputKey.PHONE_NUMBER]?.value ?: ""
            ),
            FormProfileInputKey.RW to InputTextData(
                inputName = FormProfileInputKey.RW.toString(),
                message = errors.rw,
                validations = listOf(TextValidationType.Required),
                value = profileData.input[FormProfileInputKey.RW]?.value ?: ""
            ),
            FormProfileInputKey.MOTHER_NAME to InputTextData(
                inputName = FormProfileInputKey.MOTHER_NAME.toString(),
                message = errors.namaIbu,
                validations = listOf(TextValidationType.Required),
                value = profileData.input[FormProfileInputKey.MOTHER_NAME]?.value ?: ""
            ),
            FormProfileInputKey.BLOOD_TYPE to InputTextData(
                inputName = FormProfileInputKey.BLOOD_TYPE.toString(),
                message = errors.golonganDarah,
                validations = listOf(TextValidationType.Required),
                value = profileData.input[FormProfileInputKey.BLOOD_TYPE]?.value ?: ""
            ),
            FormProfileInputKey.FULL_NAME to InputTextData(
                inputName = FormProfileInputKey.FULL_NAME.toString(),
                validations = listOf(),
                value = profileData.input[FormProfileInputKey.FULL_NAME]?.value ?: ""
            ),
            FormProfileInputKey.RELIGION to InputTextData(
                inputName = FormProfileInputKey.RELIGION.toString(),
                message = errors.agama,
                validations = listOf(TextValidationType.Required),
                value = profileData.input[FormProfileInputKey.RELIGION]?.value ?: ""
            ),
            FormProfileInputKey.LONGITUDE to InputTextData(
                inputName = FormProfileInputKey.LONGITUDE.toString(),
                message = errors.bujur,
                validations = listOf(),
                value = profileData.input[FormProfileInputKey.LONGITUDE]?.value ?: ""
            ),
            FormProfileInputKey.ID_NUMBER to InputTextData(
                validations = listOf(TextValidationType.Required),
                value = profileData.input[FormProfileInputKey.ID_NUMBER]?.value ?: "",
                inputName = FormProfileInputKey.ID_NUMBER.toString()
            ),
            FormProfileInputKey.BIRTH_PLACE to InputTextData(
                inputName = FormProfileInputKey.BIRTH_PLACE.toString(),
                message = errors.tempatLahir,
                validations = listOf(TextValidationType.Required),
                value = profileData.input[FormProfileInputKey.BIRTH_PLACE]?.value ?: "",
            ),
            FormProfileInputKey.FAMILY_RELATION to InputTextData(
                inputName = FormProfileInputKey.FAMILY_RELATION.toString(),
                message = errors?.statusHubkel,
                validations = listOf(TextValidationType.Required),
                value = profileData.input[FormProfileInputKey.FAMILY_RELATION]?.value ?: "",
            ),
            FormProfileInputKey.FAM_CARD_NUMBER to InputTextData(
                inputName = FormProfileInputKey.FAM_CARD_NUMBER.toString(),
                validations = listOf(),
                value = profileData.input[FormProfileInputKey.FAM_CARD_NUMBER]?.value ?: "",
            ),
            FormProfileInputKey.GENDER to InputTextData(
                inputName = FormProfileInputKey.GENDER.toString(),
                message = errors?.jenisKelamin,
                validations = listOf(TextValidationType.Required),
                value = profileData.input[FormProfileInputKey.GENDER]?.value ?: "",
            ),
            FormProfileInputKey.BIRTH_DATE to InputTextData(
                inputName = FormProfileInputKey.BIRTH_DATE.toString(),
                message = null,
                validations = listOf(),
                value = profileData.input[FormProfileInputKey.BIRTH_DATE]?.value ?: "",
            ),
            FormProfileInputKey.EDUCATION to InputTextData(
                inputName = FormProfileInputKey.EDUCATION.toString(),
                message = errors?.pendidikan,
                validations = listOf(TextValidationType.Required),
                value = profileData.input[FormProfileInputKey.EDUCATION]?.value ?: "",
            ),
            FormProfileInputKey.LATITUDE to InputTextData(
                inputName = FormProfileInputKey.LATITUDE.toString(),
                message = errors?.lintang,
                validations = listOf(),
                value = profileData.input[FormProfileInputKey.LATITUDE]?.value ?: "",
            ),
            FormProfileInputKey.DISTRICT to InputTextData(
                inputName = FormProfileInputKey.DISTRICT.toString(),
                message = errors?.kecamatanId,
                validations = listOf(TextValidationType.Required),
                value = profileData.input[FormProfileInputKey.DISTRICT]?.value ?: "",
            ),
            FormProfileInputKey.ADDRESS to InputTextData(
                inputName = FormProfileInputKey.ADDRESS.toString(),
                message = errors?.alamat,
                validations = listOf(TextValidationType.Required),
                value = profileData.input[FormProfileInputKey.ADDRESS]?.value ?: "",
            ),
            FormProfileInputKey.PROFESSION to InputTextData(
                inputName = FormProfileInputKey.PROFESSION.toString(),
                message = errors?.pekerjaan,
                validations = listOf(TextValidationType.Required),
                value = profileData.input[FormProfileInputKey.PROFESSION]?.value ?: "",
            ),
            FormProfileInputKey.FATHER_NAME to InputTextData(
                inputName = FormProfileInputKey.FATHER_NAME.toString(),
                message = errors?.namaAyah,
                validations = listOf(TextValidationType.Required),
                value = profileData.input[FormProfileInputKey.FATHER_NAME]?.value ?: "",
            ),
            FormProfileInputKey.MARRIAGE_STATUS to InputTextData(
                inputName = FormProfileInputKey.MARRIAGE_STATUS.toString(),
                message = errors?.statusPernikahan,
                validations = listOf(TextValidationType.Required),
                value = profileData.input[FormProfileInputKey.MARRIAGE_STATUS]?.value ?: "",
            ),
        )
    )
} ?: profileData

fun Map<FormProfileInputKey, InputTextData<TextValidationType, String>>
        .toProfileRequest(): ProfileRequest{
    return ProfileRequest(
        subDistrictId = this[FormProfileInputKey.SUB_DISTRICT]?.value ?: "",
        rt = this[FormProfileInputKey.RT]?.value ?: "",
        phoneNumber = this[FormProfileInputKey.PHONE_NUMBER]?.value ?: "",
        educationId = this[FormProfileInputKey.EDUCATION]?.value ?: "",
        rw = this[FormProfileInputKey.RW]?.value ?: "",
        motherName = this[FormProfileInputKey.MOTHER_NAME]?.value ?: "",
        bloodTypeId = this[FormProfileInputKey.BLOOD_TYPE]?.value ?: "",
        fullname = this[FormProfileInputKey.FULL_NAME]?.value ?: "",
        religionId = this[FormProfileInputKey.RELIGION]?.value ?: "",
        districtId = this[FormProfileInputKey.DISTRICT]?.value ?: "",
        address = this[FormProfileInputKey.ADDRESS]?.value ?: "",
        idNumber = this[FormProfileInputKey.ID_NUMBER]?.value ?: "",
        birthPlace = this[FormProfileInputKey.BIRTH_PLACE]?.value ?: "",
        profession = this[FormProfileInputKey.PROFESSION]?.value ?: "",
        famRelationId = this[FormProfileInputKey.FAMILY_RELATION]?.value ?: "",
        fatherName = this[FormProfileInputKey.FATHER_NAME]?.value ?: "",
        famCardNumber = this[FormProfileInputKey.FAM_CARD_NUMBER]?.value ?: "",
        genderId = this[FormProfileInputKey.GENDER]?.value ?: "",
        birthDate = this[FormProfileInputKey.BIRTH_DATE]?.value ?: "",
        marriageStatus =this[FormProfileInputKey.MARRIAGE_STATUS]?.value ?: ""
    )
}

fun ProfileResponse.toProfileData(): ProfileData {
    val errors = this.errors
    val data = this.data
    with(data){
        return ProfileData(
            id = id,
            active = active == 1,
            userId = userId,
            input = mapOf(
                FormProfileInputKey.SUB_DISTRICT to InputTextData(
                    inputName = FormProfileInputKey.SUB_DISTRICT.toString(),
                    message = errors?.desaId,
                    validations = listOf(TextValidationType.Required),
                    value = desaId.toString() ?: "",
                ),
                FormProfileInputKey.RT to InputTextData(
                        inputName = FormProfileInputKey.RT.toString(),
                    message = errors?.rt,
                    validations = listOf(TextValidationType.Required),
                    value = rt ?: ""
                ),
                FormProfileInputKey.PHONE_NUMBER to InputTextData(
                    inputName = FormProfileInputKey.PHONE_NUMBER.toString(),
                    message = errors?.noHp,
                    validations = listOf(
                        TextValidationType.Required,
                        TextValidationType.MinLength(9),
                        TextValidationType.MaxLength(15)
                    ),
                    value = noHp ?: ""
                ),
                FormProfileInputKey.RW to InputTextData(
                    inputName = FormProfileInputKey.RW.toString(),
                    message = errors?.rw,
                    validations = listOf(TextValidationType.Required),
                    value = rw ?: ""
                ),
                FormProfileInputKey.MOTHER_NAME to InputTextData(
                    inputName = FormProfileInputKey.MOTHER_NAME.toString(),
                    message = errors?.namaIbu,
                    validations = listOf(TextValidationType.Required),
                    value = namaIbu ?: ""
                ),
                FormProfileInputKey.BLOOD_TYPE to InputTextData(
                    inputName = FormProfileInputKey.BLOOD_TYPE.toString(),
                    message = errors?.golonganDarah,
                    validations = listOf(TextValidationType.Required),
                    value = golonganDarahId.toString() ?: ""
                ),
                FormProfileInputKey.FULL_NAME to InputTextData(
                    inputName = FormProfileInputKey.FULL_NAME.toString(),
                    validations = listOf(),
                    value = namaLengkap ?: ""
                ),
                FormProfileInputKey.RELIGION to InputTextData(
                    inputName = FormProfileInputKey.RELIGION.toString(),
                    message = errors?.agama,
                    validations = listOf(TextValidationType.Required),
                    value = agamaId ?: ""
                ),
                FormProfileInputKey.LONGITUDE to InputTextData(
                    inputName = FormProfileInputKey.LONGITUDE.toString(),
                    message = errors?.bujur,
                    validations = listOf(),
                    value = bujur ?: ""
                ),
                FormProfileInputKey.ID_NUMBER to InputTextData(
                    validations = listOf(TextValidationType.Required),
                    value = nik,
                    inputName = FormProfileInputKey.ID_NUMBER.toString()
                ),
                FormProfileInputKey.BIRTH_PLACE to InputTextData(
                    inputName = FormProfileInputKey.BIRTH_PLACE.toString(),
                    message = errors?.tempatLahir,
                    validations = listOf(TextValidationType.Required),
                    value = tempatLahir ?: ""
                ),
                FormProfileInputKey.FAMILY_RELATION to InputTextData(
                    inputName = FormProfileInputKey.FAMILY_RELATION.toString(),
                    message = errors?.statusHubkel,
                    validations = listOf(TextValidationType.Required),
                    value = hubunganKeluargaId ?: ""
                ),
                FormProfileInputKey.FAM_CARD_NUMBER to InputTextData(
                    inputName = FormProfileInputKey.FAM_CARD_NUMBER.toString(),
                    validations = listOf(),
                    value = noKk
                ),
                FormProfileInputKey.GENDER to InputTextData(
                    inputName = FormProfileInputKey.GENDER.toString(),
                    message = errors?.jenisKelamin,
                    validations = listOf(TextValidationType.Required),
                    value = jenisKelaminId ?: ""
                ),
                FormProfileInputKey.BIRTH_DATE to InputTextData(
                    inputName = FormProfileInputKey.BIRTH_DATE.toString(),
                    message = null,
                    validations = listOf(),
                    value = tanggalLahir ?: ""
                ),
                FormProfileInputKey.EDUCATION to InputTextData(
                    inputName = FormProfileInputKey.EDUCATION.toString(),
                    message = errors?.pendidikan,
                    validations = listOf(TextValidationType.Required),
                    value = pendidikanId ?: ""
                ),
                FormProfileInputKey.LATITUDE to InputTextData(
                    inputName = FormProfileInputKey.LATITUDE.toString(),
                    message = errors?.lintang,
                    validations = listOf(),
                    value = lintang ?: ""
                ),
                FormProfileInputKey.DISTRICT to InputTextData(
                    inputName = FormProfileInputKey.DISTRICT.toString(),
                    message = errors?.kecamatanId,
                    validations = listOf(TextValidationType.Required),
                    value = kecamatanId.toString()
                ),
                FormProfileInputKey.ADDRESS to InputTextData(
                    inputName = FormProfileInputKey.ADDRESS.toString(),
                    message = errors?.alamat,
                    validations = listOf(TextValidationType.Required),
                    value = alamat ?: ""
                ),
                FormProfileInputKey.PROFESSION to InputTextData(
                    inputName = FormProfileInputKey.PROFESSION.toString(),
                    message = errors?.pekerjaan,
                    validations = listOf(TextValidationType.Required),
                    value = pekerjaanId ?: ""
                ),
                FormProfileInputKey.FATHER_NAME to InputTextData(
                    inputName = FormProfileInputKey.FATHER_NAME.toString(),
                    message = errors?.namaAyah,
                    validations = listOf(TextValidationType.Required),
                    value = namaAyah ?: ""
                ),
                FormProfileInputKey.MARRIAGE_STATUS to InputTextData(
                    inputName = FormProfileInputKey.MARRIAGE_STATUS.toString(),
                    message = errors?.statusPernikahan,
                    validations = listOf(TextValidationType.Required),
                    value = statusPernikahanId ?: ""
                ),
            )
        )
    }
}