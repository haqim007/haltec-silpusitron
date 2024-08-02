package com.silpusitron.feature.updateprofileofficer.domain

import com.silpusitron.shared.form.domain.model.InputTextData
import com.silpusitron.shared.form.domain.model.TextValidationType


enum class FormProfileOfficerInputKey(private val key: String){
    NIP("nip"),
    NIK("nik"),
    NAME("name"),
    OPD("opd"),
    EMPLOYEE_STATUS("status_pegawai"),
    EMAIL_GOV("email_gov"),
    EMAIL("email"),
    PHONE_NUMBER("no_hp"),
    REGION_NAME("nama_wilayah"),
    REGION_TYPE("tipe_wilayah_id"),
    DISTRICT("kecamatan_id"),
    SUB_DISTRICT("desa_id");

    companion object{
        fun fromString(key: String): FormProfileOfficerInputKey {
            return entries.find { it.key == key }!!
        }
    }

    override fun toString(): String {
        return key
    }
}

data class ProfileOfficerData(
    val id: Int,
    val userId: Int,
    val fingerId: Int,
    val input: Map<FormProfileOfficerInputKey, InputTextData<TextValidationType, String>>
)

val ProfileOfficerDataDummy: ProfileOfficerData
    get() = ProfileOfficerData(
        0,
        1,
        1,
        input = mapOf(
            FormProfileOfficerInputKey.NIP to InputTextData(
                inputName = FormProfileOfficerInputKey.NIP.toString(),
                value = "ini nip",
                validations = listOf(),
                enabled = false
            ),
            FormProfileOfficerInputKey.NIK to InputTextData(
                inputName = FormProfileOfficerInputKey.NIK.toString(),
                value = "ini nik",
                validations = listOf(),
                enabled = false
            ),
            FormProfileOfficerInputKey.NAME to InputTextData(
                inputName = FormProfileOfficerInputKey.NAME.toString(),
                value = "ini name",
                validations = listOf(),
                enabled = false
            ),
            FormProfileOfficerInputKey.OPD to InputTextData(
                inputName = FormProfileOfficerInputKey.OPD.toString(),
                value = "ini OPD",
                validations = listOf(),
                enabled = false
            ),
            FormProfileOfficerInputKey.EMPLOYEE_STATUS to InputTextData(
                inputName = FormProfileOfficerInputKey.EMPLOYEE_STATUS.toString(),
                value = "ASN",
                validations = listOf(),
                enabled = false
            ),
            FormProfileOfficerInputKey.EMAIL_GOV to InputTextData(
                inputName = FormProfileOfficerInputKey.EMAIL_GOV.toString(),
                value = "ss@blitae.com",
                validations = listOf(),
                enabled = false
            ),
            FormProfileOfficerInputKey.EMAIL to InputTextData(
                inputName = FormProfileOfficerInputKey.EMAIL.toString(),
                value = "mail@bin.mail",
                validations = listOf(
                    TextValidationType.Required,
                    TextValidationType.Email
                ),
            ),
            FormProfileOfficerInputKey.PHONE_NUMBER to InputTextData(
                inputName = FormProfileOfficerInputKey.PHONE_NUMBER.toString(),
                value = "83858483821",
                validations = listOf(
                    TextValidationType.Required,
                    TextValidationType.MinLength(11),
                    TextValidationType.MaxLength(15)
                )
            ),
            FormProfileOfficerInputKey.REGION_NAME to InputTextData(
                inputName = FormProfileOfficerInputKey.REGION_NAME.toString(),
                value = "ini WILAYAH",
                validations = listOf(),
                enabled = false
            ),
            FormProfileOfficerInputKey.REGION_TYPE to InputTextData(
                inputName = FormProfileOfficerInputKey.REGION_TYPE.toString(),
                value = "ini jenis wilayah ID",
                validations = listOf(),
                enabled = false
            ),
            FormProfileOfficerInputKey.DISTRICT to InputTextData(
                inputName = FormProfileOfficerInputKey.DISTRICT.toString(),
                value = "ini district",
                validations = listOf(),
                enabled = false
            ), FormProfileOfficerInputKey.SUB_DISTRICT to InputTextData(
                inputName = FormProfileOfficerInputKey.SUB_DISTRICT.toString(),
                value = "ini sub district",
                validations = listOf(),
                enabled = false
            )
        )
    )
