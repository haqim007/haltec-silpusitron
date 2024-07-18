package com.haltec.silpusitron.feature.requirementdocs.common.data.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LetterLevelsResponse(
    val data: List<LetterLevelResponse>
)

@Serializable
data class LetterLevelResponse(
    @SerialName("level")
    val level: String,

    @SerialName("level_surat")
    val levelSurat: String

)
