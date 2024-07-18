package com.haltec.silpusitron.feature.requirementdocs.common.data.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LetterTypesResponse(
    val data: List<LetterTypeResponse>
)

@Serializable
data class LetterTypeResponse(
    @SerialName("jenis_surat")
    val jenisSurat: String,

    @SerialName("jenis_surat_id")
    val jenisSuratId: Int,
)
