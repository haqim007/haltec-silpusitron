package com.haltec.silpusitron.shared.formprofile.data

import com.haltec.silpusitron.shared.form.domain.model.InputTextData
import com.haltec.silpusitron.shared.formprofile.data.remote.response.ProfileResponse
import com.haltec.silpusitron.shared.formprofile.domain.model.FormProfileInputKey
import com.haltec.silpusitron.shared.formprofile.domain.model.ProfileData


val validationsInput: Map<FormProfileInputKey, List<com.haltec.silpusitron.shared.form.domain.model.TextValidationType>> = mapOf(
    FormProfileInputKey.SUB_DISTRICT to listOf(com.haltec.silpusitron.shared.form.domain.model.TextValidationType.Required),
    FormProfileInputKey.RT to listOf(com.haltec.silpusitron.shared.form.domain.model.TextValidationType.Required),
    FormProfileInputKey.PHONE_NUMBER to listOf(
        com.haltec.silpusitron.shared.form.domain.model.TextValidationType.Required,
        com.haltec.silpusitron.shared.form.domain.model.TextValidationType.MinLength(11),
        com.haltec.silpusitron.shared.form.domain.model.TextValidationType.MaxLength(15)
    ),
    FormProfileInputKey.RW to listOf(com.haltec.silpusitron.shared.form.domain.model.TextValidationType.Required),
    FormProfileInputKey.MOTHER_NAME to listOf(com.haltec.silpusitron.shared.form.domain.model.TextValidationType.Required),
    FormProfileInputKey.BLOOD_TYPE to listOf(com.haltec.silpusitron.shared.form.domain.model.TextValidationType.Required),
    FormProfileInputKey.FULL_NAME to listOf(),
    FormProfileInputKey.RELIGION to listOf(com.haltec.silpusitron.shared.form.domain.model.TextValidationType.Required),
    FormProfileInputKey.LONGITUDE to listOf(),
    FormProfileInputKey.ID_NUMBER to listOf(com.haltec.silpusitron.shared.form.domain.model.TextValidationType.Required),
    FormProfileInputKey.BIRTH_PLACE to listOf(com.haltec.silpusitron.shared.form.domain.model.TextValidationType.Required),
    FormProfileInputKey.FAMILY_RELATION to listOf(com.haltec.silpusitron.shared.form.domain.model.TextValidationType.Required),
    FormProfileInputKey.FAM_CARD_NUMBER to listOf(),
    FormProfileInputKey.GENDER to listOf(com.haltec.silpusitron.shared.form.domain.model.TextValidationType.Required),
    FormProfileInputKey.BIRTH_DATE to listOf(),
    FormProfileInputKey.EDUCATION to listOf(com.haltec.silpusitron.shared.form.domain.model.TextValidationType.Required),
    FormProfileInputKey.LATITUDE to listOf(),
    FormProfileInputKey.DISTRICT to listOf(com.haltec.silpusitron.shared.form.domain.model.TextValidationType.Required),
    FormProfileInputKey.ADDRESS to listOf(com.haltec.silpusitron.shared.form.domain.model.TextValidationType.Required),
    FormProfileInputKey.PROFESSION to listOf(com.haltec.silpusitron.shared.form.domain.model.TextValidationType.Required),
    FormProfileInputKey.FATHER_NAME to listOf(com.haltec.silpusitron.shared.form.domain.model.TextValidationType.Required),
    FormProfileInputKey.MARRIAGE_STATUS to listOf(com.haltec.silpusitron.shared.form.domain.model.TextValidationType.Required),
)


internal fun ProfileResponse.toProfileData(): ProfileData {
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
                    validations = validationsInput[FormProfileInputKey.SUB_DISTRICT]!!,
                    value = desaId.toString() ?: "",
                ),
                FormProfileInputKey.RT to InputTextData(
                    inputName = FormProfileInputKey.RT.toString(),
                    message = errors?.rt,
                    validations = validationsInput[FormProfileInputKey.RT]!!,
                    value = rt ?: ""
                ),
                FormProfileInputKey.PHONE_NUMBER to InputTextData(
                    inputName = FormProfileInputKey.PHONE_NUMBER.toString(),
                    message = errors?.noHp,
                    validations = validationsInput[FormProfileInputKey.PHONE_NUMBER]!!,
                    value = noHp ?: ""
                ),
                FormProfileInputKey.RW to InputTextData(
                    inputName = FormProfileInputKey.RW.toString(),
                    message = errors?.rw,
                    validations = validationsInput[FormProfileInputKey.RW]!!,
                    value = rw ?: ""
                ),
                FormProfileInputKey.MOTHER_NAME to InputTextData(
                    inputName = FormProfileInputKey.MOTHER_NAME.toString(),
                    message = errors?.namaIbu,
                    validations = validationsInput[FormProfileInputKey.MOTHER_NAME]!!,
                    value = namaIbu ?: ""
                ),
                FormProfileInputKey.BLOOD_TYPE to InputTextData(
                    inputName = FormProfileInputKey.BLOOD_TYPE.toString(),
                    message = errors?.golonganDarah,
                    validations = validationsInput[FormProfileInputKey.BLOOD_TYPE]!!,
                    value = golonganDarahId.toString() ?: ""
                ),
                FormProfileInputKey.FULL_NAME to InputTextData(
                    inputName = FormProfileInputKey.FULL_NAME.toString(),
                    validations = validationsInput[FormProfileInputKey.FULL_NAME]!!,
                    value = namaLengkap ?: ""
                ),
                FormProfileInputKey.RELIGION to InputTextData(
                    inputName = FormProfileInputKey.RELIGION.toString(),
                    message = errors?.agama,
                    validations = validationsInput[FormProfileInputKey.RELIGION]!!,
                    value = agamaId ?: ""
                ),
                FormProfileInputKey.LONGITUDE to InputTextData(
                    inputName = FormProfileInputKey.LONGITUDE.toString(),
                    message = errors?.bujur,
                    validations = validationsInput[FormProfileInputKey.LONGITUDE]!!,
                    value = bujur ?: ""
                ),
                FormProfileInputKey.ID_NUMBER to InputTextData(
                    validations = validationsInput[FormProfileInputKey.ID_NUMBER]!!,
                    value = nik,
                    inputName = FormProfileInputKey.ID_NUMBER.toString()
                ),
                FormProfileInputKey.BIRTH_PLACE to InputTextData(
                    inputName = FormProfileInputKey.BIRTH_PLACE.toString(),
                    message = errors?.tempatLahir,
                    validations = validationsInput[FormProfileInputKey.BIRTH_PLACE]!!,
                    value = tempatLahir ?: ""
                ),
                FormProfileInputKey.FAMILY_RELATION to InputTextData(
                    inputName = FormProfileInputKey.FAMILY_RELATION.toString(),
                    message = errors?.statusHubkel,
                    validations = validationsInput[FormProfileInputKey.FAMILY_RELATION]!!,
                    value = hubunganKeluargaId ?: ""
                ),
                FormProfileInputKey.FAM_CARD_NUMBER to InputTextData(
                    inputName = FormProfileInputKey.FAM_CARD_NUMBER.toString(),
                    validations = validationsInput[FormProfileInputKey.FAM_CARD_NUMBER]!!,
                    value = noKk
                ),
                FormProfileInputKey.GENDER to InputTextData(
                    inputName = FormProfileInputKey.GENDER.toString(),
                    message = errors?.jenisKelamin,
                    validations = validationsInput[FormProfileInputKey.GENDER]!!,
                    value = jenisKelaminId ?: ""
                ),
                FormProfileInputKey.BIRTH_DATE to InputTextData(
                    inputName = FormProfileInputKey.BIRTH_DATE.toString(),
                    message = null,
                    validations = validationsInput[FormProfileInputKey.BIRTH_DATE]!!,
                    value = tanggalLahir ?: ""
                ),
                FormProfileInputKey.EDUCATION to InputTextData(
                    inputName = FormProfileInputKey.EDUCATION.toString(),
                    message = errors?.pendidikan,
                    validations = validationsInput[FormProfileInputKey.EDUCATION]!!,
                    value = pendidikanId ?: ""
                ),
                FormProfileInputKey.LATITUDE to InputTextData(
                    inputName = FormProfileInputKey.LATITUDE.toString(),
                    message = errors?.lintang,
                    validations = validationsInput[FormProfileInputKey.LATITUDE]!!,
                    value = lintang ?: ""
                ),
                FormProfileInputKey.DISTRICT to InputTextData(
                    inputName = FormProfileInputKey.DISTRICT.toString(),
                    message = errors?.kecamatanId,
                    validations = validationsInput[FormProfileInputKey.DISTRICT]!!,
                    value = kecamatanId.toString()
                ),
                FormProfileInputKey.ADDRESS to InputTextData(
                    inputName = FormProfileInputKey.ADDRESS.toString(),
                    message = errors?.alamat,
                    validations = validationsInput[FormProfileInputKey.ADDRESS]!!,
                    value = alamat ?: ""
                ),
                FormProfileInputKey.PROFESSION to InputTextData(
                    inputName = FormProfileInputKey.PROFESSION.toString(),
                    message = errors?.pekerjaan,
                    validations = validationsInput[FormProfileInputKey.PROFESSION]!!,
                    value = pekerjaanId ?: ""
                ),
                FormProfileInputKey.FATHER_NAME to InputTextData(
                    inputName = FormProfileInputKey.FATHER_NAME.toString(),
                    message = errors?.namaAyah,
                    validations = validationsInput[FormProfileInputKey.FATHER_NAME]!!,
                    value = namaAyah ?: ""
                ),
                FormProfileInputKey.MARRIAGE_STATUS to InputTextData(
                    inputName = FormProfileInputKey.MARRIAGE_STATUS.toString(),
                    message = errors?.statusPernikahan,
                    validations = validationsInput[FormProfileInputKey.MARRIAGE_STATUS]!!,
                    value = statusPernikahanId ?: ""
                ),
            )
        )
    }
}