package com.haltec.silpusitron.shared.formprofile.data.dummy

import com.haltec.silpusitron.shared.form.domain.model.InputTextData
import com.haltec.silpusitron.shared.form.domain.model.TextValidationType
import com.haltec.silpusitron.shared.formprofile.domain.model.FormProfileInputKey


val formProfileInputDummy: Map<FormProfileInputKey, InputTextData<TextValidationType, String>> = mapOf(
    FormProfileInputKey.MOTHER_NAME to InputTextData(
        inputName = FormProfileInputKey.MOTHER_NAME.toString(),
        value = "",
        validations = listOf(
            TextValidationType.Required,
        )
    ),
    FormProfileInputKey.SUB_DISTRICT to InputTextData(
        inputName = FormProfileInputKey.SUB_DISTRICT.toString(),
        validations = listOf(TextValidationType.Required),
        value = "",
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
    FormProfileInputKey.RT to InputTextData(
        inputName = FormProfileInputKey.RT.toString(),
        validations = listOf(TextValidationType.Required),
        value = ""
    ),
    FormProfileInputKey.PHONE_NUMBER to InputTextData(
        inputName = FormProfileInputKey.PHONE_NUMBER.toString(),
        validations = listOf(
            TextValidationType.Required,
            TextValidationType.MaxLength(13)
        ),
        value = ""
    ),
    FormProfileInputKey.RW to InputTextData(
        inputName = FormProfileInputKey.RW.toString(),
        validations = listOf(TextValidationType.Required),
        value = ""
    ),
    FormProfileInputKey.BLOOD_TYPE to InputTextData(
        inputName = FormProfileInputKey.BLOOD_TYPE.toString(),
        validations = listOf(TextValidationType.Required),
        value = "",
        options = listOf(
            InputTextData.Option(
                value = "8",
                label = "A",
                key = "GOLONGAN_DARAH"
            ),
            InputTextData.Option(
                value = "9",
                label = "A-",
                key = "GOLONGAN_DARAH"
            )
        )
    ),
    FormProfileInputKey.FULL_NAME to InputTextData(
        inputName = FormProfileInputKey.FULL_NAME.toString(),
        validations = listOf(),
        value = ""
    ),
    FormProfileInputKey.RELIGION to InputTextData(
        inputName = FormProfileInputKey.RELIGION.toString(),
        validations = listOf(TextValidationType.Required),
        value = "",
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
    FormProfileInputKey.LONGITUDE to InputTextData(
        inputName = FormProfileInputKey.LONGITUDE.toString(),
        validations = listOf(),
        value = ""
    ),
    FormProfileInputKey.ID_NUMBER to InputTextData(
        validations = listOf(TextValidationType.Required),
        value = "",
        inputName = FormProfileInputKey.ID_NUMBER.toString()
    ),
    FormProfileInputKey.BIRTH_PLACE to InputTextData(
        inputName = FormProfileInputKey.BIRTH_PLACE.toString(),
        validations = listOf(TextValidationType.Required),
        value = "tempatLahir"
    ),
    FormProfileInputKey.FAMILY_RELATION to InputTextData(
        inputName = FormProfileInputKey.FAMILY_RELATION.toString(),
        validations = listOf(TextValidationType.Required),
        value = "",
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
    ),
    FormProfileInputKey.FAM_CARD_NUMBER to InputTextData(
        inputName = FormProfileInputKey.FAM_CARD_NUMBER.toString(),
        validations = listOf(),
        value = "noKk"
    ),
    FormProfileInputKey.GENDER to InputTextData(
        inputName = FormProfileInputKey.GENDER.toString(),
        validations = listOf(TextValidationType.Required),
        value = "",
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
    FormProfileInputKey.BIRTH_DATE to InputTextData(
        inputName = FormProfileInputKey.BIRTH_DATE.toString(),
        validations = listOf(),
        value = "09 September 2009"
    ),
    FormProfileInputKey.EDUCATION to InputTextData(
        inputName = FormProfileInputKey.EDUCATION.toString(),
        validations = listOf(TextValidationType.Required),
        value = "",
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
    FormProfileInputKey.LATITUDE to InputTextData(
        inputName = FormProfileInputKey.LATITUDE.toString(),
        validations = listOf(),
        value = "lintang"
    ),
    FormProfileInputKey.DISTRICT to InputTextData(
        inputName = FormProfileInputKey.DISTRICT.toString(),
        validations = listOf(TextValidationType.Required),
        value = "",
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
    FormProfileInputKey.ADDRESS to InputTextData(
        inputName = FormProfileInputKey.ADDRESS.toString(),
        validations = listOf(TextValidationType.Required),
        value = ""
    ),
    FormProfileInputKey.PROFESSION to InputTextData(
        inputName = FormProfileInputKey.PROFESSION.toString(),
        validations = listOf(TextValidationType.Required),
        value = "",
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
    FormProfileInputKey.FATHER_NAME to InputTextData(
        inputName = FormProfileInputKey.FATHER_NAME.toString(),
        validations = listOf(TextValidationType.Required),
        value = ""
    ),
    FormProfileInputKey.MARRIAGE_STATUS to InputTextData(
        inputName = FormProfileInputKey.MARRIAGE_STATUS.toString(),
        validations = listOf(TextValidationType.Required),
        value = "",
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
    )
)