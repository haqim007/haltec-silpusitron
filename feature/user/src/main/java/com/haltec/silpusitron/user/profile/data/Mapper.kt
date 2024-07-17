package com.haltec.silpusitron.user.profile.data

import com.haltec.silpusitron.shared.form.domain.model.InputTextData
import com.haltec.silpusitron.shared.form.domain.model.TextValidationType
import com.haltec.silpusitron.shared.formprofile.data.validationsInput
import com.haltec.silpusitron.shared.formprofile.domain.model.FormProfileInputKey
import com.haltec.silpusitron.shared.formprofile.domain.model.ProfileData
import com.haltec.silpusitron.user.profile.data.remote.request.ProfileRequest
import com.haltec.silpusitron.user.profile.data.remote.response.SubmitProfileResponse

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
                validations = validationsInput[FormProfileInputKey.SUB_DISTRICT]!!,
                value = profileData.input[FormProfileInputKey.SUB_DISTRICT]?.value ?: "",
            ),
            FormProfileInputKey.RT to InputTextData(
                inputName = FormProfileInputKey.RT.toString(),
                message = errors.rt,
                validations = validationsInput[FormProfileInputKey.RT]!!,
                value = profileData.input[FormProfileInputKey.RT]?.value ?: ""
            ),
            FormProfileInputKey.PHONE_NUMBER to InputTextData(
                inputName = FormProfileInputKey.PHONE_NUMBER.toString(),
                message = errors.noHp,
                validations = validationsInput[FormProfileInputKey.PHONE_NUMBER]!!,
                value = profileData.input[FormProfileInputKey.PHONE_NUMBER]?.value ?: ""
            ),
            FormProfileInputKey.RW to InputTextData(
                inputName = FormProfileInputKey.RW.toString(),
                message = errors.rw,
                validations = validationsInput[FormProfileInputKey.RW]!!,
                value = profileData.input[FormProfileInputKey.RW]?.value ?: ""
            ),
            FormProfileInputKey.MOTHER_NAME to InputTextData(
                inputName = FormProfileInputKey.MOTHER_NAME.toString(),
                message = errors.namaIbu,
                validations = validationsInput[FormProfileInputKey.MOTHER_NAME]!!,
                value = profileData.input[FormProfileInputKey.MOTHER_NAME]?.value ?: ""
            ),
            FormProfileInputKey.BLOOD_TYPE to InputTextData(
                inputName = FormProfileInputKey.BLOOD_TYPE.toString(),
                message = errors.golonganDarah,
                validations = validationsInput[FormProfileInputKey.BLOOD_TYPE]!!,
                value = profileData.input[FormProfileInputKey.BLOOD_TYPE]?.value ?: ""
            ),
            FormProfileInputKey.FULL_NAME to InputTextData(
                inputName = FormProfileInputKey.FULL_NAME.toString(),
                validations = validationsInput[FormProfileInputKey.BLOOD_TYPE]!!,
                value = profileData.input[FormProfileInputKey.FULL_NAME]?.value ?: ""
            ),
            FormProfileInputKey.RELIGION to InputTextData(
                inputName = FormProfileInputKey.RELIGION.toString(),
                message = errors.agama,
                validations = validationsInput[FormProfileInputKey.RELIGION]!!,
                value = profileData.input[FormProfileInputKey.RELIGION]?.value ?: ""
            ),
            FormProfileInputKey.LONGITUDE to InputTextData(
                inputName = FormProfileInputKey.LONGITUDE.toString(),
                message = errors.bujur,
                validations = validationsInput[FormProfileInputKey.LONGITUDE]!!,
                value = profileData.input[FormProfileInputKey.LONGITUDE]?.value ?: ""
            ),
            FormProfileInputKey.ID_NUMBER to InputTextData(
                validations = validationsInput[FormProfileInputKey.ID_NUMBER]!!,
                value = profileData.input[FormProfileInputKey.ID_NUMBER]?.value ?: "",
                inputName = FormProfileInputKey.ID_NUMBER.toString(),
                message = errors.nik,
            ),
            FormProfileInputKey.BIRTH_PLACE to InputTextData(
                inputName = FormProfileInputKey.BIRTH_PLACE.toString(),
                message = errors.tempatLahir,
                validations = validationsInput[FormProfileInputKey.BIRTH_PLACE]!!,
                value = profileData.input[FormProfileInputKey.BIRTH_PLACE]?.value ?: "",
            ),
            FormProfileInputKey.FAMILY_RELATION to InputTextData(
                inputName = FormProfileInputKey.FAMILY_RELATION.toString(),
                message = errors.statusHubkel,
                validations = validationsInput[FormProfileInputKey.FAMILY_RELATION]!!,
                value = profileData.input[FormProfileInputKey.FAMILY_RELATION]?.value ?: "",
            ),
            FormProfileInputKey.FAM_CARD_NUMBER to InputTextData(
                inputName = FormProfileInputKey.FAM_CARD_NUMBER.toString(),
                validations = validationsInput[FormProfileInputKey.FAM_CARD_NUMBER]!!,
                value = profileData.input[FormProfileInputKey.FAM_CARD_NUMBER]?.value ?: "",
            ),
            FormProfileInputKey.GENDER to InputTextData(
                inputName = FormProfileInputKey.GENDER.toString(),
                message = errors.jenisKelamin,
                validations = validationsInput[FormProfileInputKey.GENDER]!!,
                value = profileData.input[FormProfileInputKey.GENDER]?.value ?: "",
            ),
            FormProfileInputKey.BIRTH_DATE to InputTextData(
                inputName = FormProfileInputKey.BIRTH_DATE.toString(),
                message = null,
                validations = validationsInput[FormProfileInputKey.BIRTH_DATE]!!,
                value = profileData.input[FormProfileInputKey.BIRTH_DATE]?.value ?: "",
            ),
            FormProfileInputKey.EDUCATION to InputTextData(
                inputName = FormProfileInputKey.EDUCATION.toString(),
                message = errors.pendidikan,
                validations = validationsInput[FormProfileInputKey.EDUCATION]!!,
                value = profileData.input[FormProfileInputKey.EDUCATION]?.value ?: "",
            ),
            FormProfileInputKey.LATITUDE to InputTextData(
                inputName = FormProfileInputKey.LATITUDE.toString(),
                message = errors.lintang,
                validations = validationsInput[FormProfileInputKey.LATITUDE]!!,
                value = profileData.input[FormProfileInputKey.LATITUDE]?.value ?: "",
            ),
            FormProfileInputKey.DISTRICT to InputTextData(
                inputName = FormProfileInputKey.DISTRICT.toString(),
                message = errors.kecamatanId,
                validations = validationsInput[FormProfileInputKey.DISTRICT]!!,
                value = profileData.input[FormProfileInputKey.DISTRICT]?.value ?: "",
            ),
            FormProfileInputKey.ADDRESS to InputTextData(
                inputName = FormProfileInputKey.ADDRESS.toString(),
                message = errors.alamat,
                validations = validationsInput[FormProfileInputKey.ADDRESS]!!,
                value = profileData.input[FormProfileInputKey.ADDRESS]?.value ?: "",
            ),
            FormProfileInputKey.PROFESSION to InputTextData(
                inputName = FormProfileInputKey.PROFESSION.toString(),
                message = errors.pekerjaan,
                validations = validationsInput[FormProfileInputKey.PROFESSION]!!,
                value = profileData.input[FormProfileInputKey.PROFESSION]?.value ?: "",
            ),
            FormProfileInputKey.FATHER_NAME to InputTextData(
                inputName = FormProfileInputKey.FATHER_NAME.toString(),
                message = errors.namaAyah,
                validations = validationsInput[FormProfileInputKey.FATHER_NAME]!!,
                value = profileData.input[FormProfileInputKey.FATHER_NAME]?.value ?: "",
            ),
            FormProfileInputKey.MARRIAGE_STATUS to InputTextData(
                inputName = FormProfileInputKey.MARRIAGE_STATUS.toString(),
                message = errors.statusPernikahan,
                validations = validationsInput[FormProfileInputKey.MARRIAGE_STATUS]!!,
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
