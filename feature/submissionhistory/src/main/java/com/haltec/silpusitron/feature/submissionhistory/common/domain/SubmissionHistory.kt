package com.haltec.silpusitron.feature.submissionhistory.common.domain

import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
@Parcelize
data class SubmissionHistory(
    val title: String,
    val number: String,
    val submissionDate: String,
    val proceedBy: String,
    val status: String,
    val id: Int,
    val isFinish: Boolean,
    val fileUrl: String?
): Parcelable

val SubmissionHistoryType = object : NavType<SubmissionHistory>(
    isNullableAllowed = false
) {
    override fun put(bundle: Bundle, key: String, value: SubmissionHistory) {
        bundle.putParcelable(key, value)
    }
    override fun get(bundle: Bundle, key: String): SubmissionHistory {
        return bundle.getParcelable<SubmissionHistory>(key) as SubmissionHistory
    }

    override fun serializeAsValue(value: SubmissionHistory): String {
        // Serialized values must always be Uri encoded
        return Uri.encode(Json.encodeToString(value))
    }

    override fun parseValue(value: String): SubmissionHistory {
        // Navigation takes care of decoding the string
        // before passing it to parseValue()
        return Json.decodeFromString<SubmissionHistory>(value)
    }

}

internal val SubmissionHistoryDummies = listOf(
    SubmissionHistory(
        title = "Pengajuan Surat Kehilangan",
        number = "P20240312001",
        submissionDate = "12 Mar 2024",
        proceedBy = "Sekda",
        status = "Ter TTE",
        id = 1,
        isFinish = false,
        fileUrl = null
    ),
    SubmissionHistory(
        title = "Pengajuan Surat Kehilangan",
        number = "P20240312001",
        submissionDate = "12 Mar 2024",
        proceedBy = "Sekda",
        status = "Ter TTE",
        id = 2,
        isFinish = true,
        fileUrl = ""
    ),
    SubmissionHistory(
        title = "Pengajuan Surat Kehilangan",
        number = "P20240312001",
        submissionDate = "12 Mar 2024",
        proceedBy = "Sekda",
        status = "Ter TTE",
        id = 3,
        isFinish = false,
        fileUrl = null
    )
)