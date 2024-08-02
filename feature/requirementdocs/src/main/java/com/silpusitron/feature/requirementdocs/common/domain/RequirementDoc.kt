package com.silpusitron.feature.requirementdocs.common.domain

typealias DocName = String

data class RequirementDoc(
    val requirementDocs: List<DocName> = listOf(),
    val letterType: String,
    val isTt1: Int,
    val isTt2: Int,
    val level: String,
    val letterLevel: String,
    val id: Int,
    val letterTypeId: Int,
    val title: String
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
        isTt1 = 1,
        isTt2 = 0,
    )
)