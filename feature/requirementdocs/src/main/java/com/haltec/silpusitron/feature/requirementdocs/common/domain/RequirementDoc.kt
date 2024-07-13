package com.haltec.silpusitron.feature.requirementdocs.common.domain

import com.haltec.silpusitron.shared.form.domain.model.FileValidationType
import com.haltec.silpusitron.shared.form.domain.model.InputTextData
import com.haltec.silpusitron.shared.form.domain.model.TextValidationType
import java.io.File

typealias DocName = String
typealias FieldName = Int
typealias DocId = Int
data class RequirementDoc(
    val requirementDocs: List<DocName> = listOf(),
    val letterType: String,
    val isTt1: Int,
    val isTt2: Int,
    val level: String,
    val letterLevel: String,
    val id: Int,
    val letterTypeId: Int,
    val title: String,
    val inputForms: Map<FieldName, InputTextData<TextValidationType, String>>,
    val inputFiles: Map<DocId, InputTextData<FileValidationType, File>>
)


val requirementDocDummies = listOf(
    RequirementDoc(
        requirementDocs = listOf(
            "berkas AKTE TANAH",
            "berkas KK",
            "berkas KTP"
        ),
        id = 6,
        title = "SURAT REKOMENDASI PEMBELIAN BBM JENIS TERTENTU 1",
        letterLevel = "Kelurahan",
        level = "level_1_kelurahan",
        letterType = "Resmi",
        letterTypeId = 9,
        inputFiles = mapOf(
            7 to InputTextData(
                inputName = "7",
                validations = listOf(FileValidationType.Required),
                value = null,
                inputLabel = "berkas AKTE TANAH"
            ),
            6 to InputTextData(
                inputName = "6",
                validations = listOf(FileValidationType.Required),
                value = null,
                inputLabel = "berkas KK"
            ),
            5 to InputTextData(
                inputName = "5",
                validations = listOf(FileValidationType.Required),
                value = null,
                inputLabel = "berkas KTP"
            )
        ),
        inputForms = mapOf(
            15 to InputTextData(
                inputName = "15",
                validations = listOf(TextValidationType.Required),
                value = "",
                inputLabel = "alamat usaha"
            ),
            16 to InputTextData(
                inputName = "15",
                validations = listOf(TextValidationType.Required),
                value = "",
                inputLabel = "jenis usaha kegiatan"
            ),
            17 to InputTextData(
                inputName = "17",
                validations = listOf(TextValidationType.Required),
                value = "",
                inputLabel = "data sample"
            )
        ),
        isTt1 = 1,
        isTt2 = 0,
    ),
    RequirementDoc(
        requirementDocs = listOf(
            "berkas AKTE TANAH",
            "berkas KK",
            "berkas KTP"
        ),
        id = 7,
        title = "SURAT REKOMENDASI PEMBELIAN BBM JENIS TERTENTU 2",
        letterLevel = "Kelurahan",
        level = "level_1_kelurahan",
        letterType = "Resmi",
        letterTypeId = 9,
        inputFiles = mapOf(
            7 to InputTextData(
                inputName = "7",
                validations = listOf(FileValidationType.Required),
                value = null,
                inputLabel = "berkas AKTE TANAH"
            ),
            6 to InputTextData(
                inputName = "6",
                validations = listOf(FileValidationType.Required),
                value = null,
                inputLabel = "berkas KK"
            ),
            5 to InputTextData(
                inputName = "5",
                validations = listOf(FileValidationType.Required),
                value = null,
                inputLabel = "berkas KTP"
            )
        ),
        inputForms = mapOf(
            15 to InputTextData(
                inputName = "15",
                validations = listOf(TextValidationType.Required),
                value = "",
                inputLabel = "alamat usaha"
            ),
            16 to InputTextData(
                inputName = "15",
                validations = listOf(TextValidationType.Required),
                value = "",
                inputLabel = "jenis usaha kegiatan"
            ),
            17 to InputTextData(
                inputName = "17",
                validations = listOf(TextValidationType.Required),
                value = "",
                inputLabel = "data sample"
            )
        ),
        isTt1 = 1,
        isTt2 = 0,
    ),
    RequirementDoc(
        requirementDocs = listOf(
            "berkas AKTE TANAH",
            "berkas KK",
            "berkas KTP"
        ),
        id = 8,
        title = "SURAT REKOMENDASI PEMBELIAN BBM JENIS TERTENTU 3",
        letterLevel = "Kelurahan",
        level = "level_1_kelurahan",
        letterType = "Resmi",
        letterTypeId = 9,
        inputFiles = mapOf(
            7 to InputTextData(
                inputName = "7",
                validations = listOf(FileValidationType.Required),
                value = null,
                inputLabel = "berkas AKTE TANAH"
            ),
            6 to InputTextData(
                inputName = "6",
                validations = listOf(FileValidationType.Required),
                value = null,
                inputLabel = "berkas KK"
            ),
            5 to InputTextData(
                inputName = "5",
                validations = listOf(FileValidationType.Required),
                value = null,
                inputLabel = "berkas KTP"
            )
        ),
        inputForms = mapOf(
            15 to InputTextData(
                inputName = "15",
                validations = listOf(TextValidationType.Required),
                value = "",
                inputLabel = "alamat usaha"
            ),
            16 to InputTextData(
                inputName = "15",
                validations = listOf(TextValidationType.Required),
                value = "",
                inputLabel = "jenis usaha kegiatan"
            ),
            17 to InputTextData(
                inputName = "17",
                validations = listOf(TextValidationType.Required),
                value = "",
                inputLabel = "data sample"
            )
        ),
        isTt1 = 1,
        isTt2 = 0,
    ),
    RequirementDoc(
        requirementDocs = listOf(
            "berkas AKTE TANAH",
            "berkas KK",
            "berkas KTP"
        ),
        id = 9,
        title = "SURAT REKOMENDASI PEMBELIAN BBM JENIS TERTENTU 4",
        letterLevel = "Kelurahan",
        level = "level_1_kelurahan",
        letterType = "Resmi",
        letterTypeId = 9,
        inputFiles = mapOf(
            7 to InputTextData(
                inputName = "7",
                validations = listOf(FileValidationType.Required),
                value = null,
                inputLabel = "berkas AKTE TANAH"
            ),
            6 to InputTextData(
                inputName = "6",
                validations = listOf(FileValidationType.Required),
                value = null,
                inputLabel = "berkas KK"
            ),
            5 to InputTextData(
                inputName = "5",
                validations = listOf(FileValidationType.Required),
                value = null,
                inputLabel = "berkas KTP"
            )
        ),
        inputForms = mapOf(
            15 to InputTextData(
                inputName = "15",
                validations = listOf(TextValidationType.Required),
                value = "",
                inputLabel = "alamat usaha"
            ),
            16 to InputTextData(
                inputName = "15",
                validations = listOf(TextValidationType.Required),
                value = "",
                inputLabel = "jenis usaha kegiatan"
            ),
            17 to InputTextData(
                inputName = "17",
                validations = listOf(TextValidationType.Required),
                value = "",
                inputLabel = "data sample"
            )
        ),
        isTt1 = 1,
        isTt2 = 0,
    ),
    RequirementDoc(
        requirementDocs = listOf(
            "berkas AKTE TANAH",
            "berkas KK",
            "berkas KTP"
        ),
        id = 10,
        title = "SURAT REKOMENDASI PEMBELIAN BBM JENIS TERTENTU 5",
        letterLevel = "Kelurahan",
        level = "level_1_kelurahan",
        letterType = "Resmi",
        letterTypeId = 9,
        inputFiles = mapOf(
            7 to InputTextData(
                inputName = "7",
                validations = listOf(FileValidationType.Required),
                value = null,
                inputLabel = "berkas AKTE TANAH"
            ),
            6 to InputTextData(
                inputName = "6",
                validations = listOf(FileValidationType.Required),
                value = null,
                inputLabel = "berkas KK"
            ),
            5 to InputTextData(
                inputName = "5",
                validations = listOf(FileValidationType.Required),
                value = null,
                inputLabel = "berkas KTP"
            )
        ),
        inputForms = mapOf(
            15 to InputTextData(
                inputName = "15",
                validations = listOf(TextValidationType.Required),
                value = "",
                inputLabel = "alamat usaha"
            ),
            16 to InputTextData(
                inputName = "15",
                validations = listOf(TextValidationType.Required),
                value = "",
                inputLabel = "jenis usaha kegiatan"
            ),
            17 to InputTextData(
                inputName = "17",
                validations = listOf(TextValidationType.Required),
                value = "",
                inputLabel = "data sample"
            )
        ),
        isTt1 = 1,
        isTt2 = 0,
    ),
    RequirementDoc(
        requirementDocs = listOf(
            "berkas AKTE TANAH",
            "berkas KK",
            "berkas KTP"
        ),
        id = 11,
        title = "SURAT REKOMENDASI PEMBELIAN BBM JENIS TERTENTU 6",
        letterLevel = "Kelurahan",
        level = "level_1_kelurahan",
        letterType = "Resmi",
        letterTypeId = 9,
        inputFiles = mapOf(
            7 to InputTextData(
                inputName = "7",
                validations = listOf(FileValidationType.Required),
                value = null,
                inputLabel = "berkas AKTE TANAH"
            ),
            6 to InputTextData(
                inputName = "6",
                validations = listOf(FileValidationType.Required),
                value = null,
                inputLabel = "berkas KK"
            ),
            5 to InputTextData(
                inputName = "5",
                validations = listOf(FileValidationType.Required),
                value = null,
                inputLabel = "berkas KTP"
            )
        ),
        inputForms = mapOf(
            15 to InputTextData(
                inputName = "15",
                validations = listOf(TextValidationType.Required),
                value = "",
                inputLabel = "alamat usaha"
            ),
            16 to InputTextData(
                inputName = "15",
                validations = listOf(TextValidationType.Required),
                value = "",
                inputLabel = "jenis usaha kegiatan"
            ),
            17 to InputTextData(
                inputName = "17",
                validations = listOf(TextValidationType.Required),
                value = "",
                inputLabel = "data sample"
            )
        ),
        isTt1 = 1,
        isTt2 = 0,
    )
)