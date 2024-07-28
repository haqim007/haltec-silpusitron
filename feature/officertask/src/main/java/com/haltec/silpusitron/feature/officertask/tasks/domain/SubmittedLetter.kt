package com.haltec.silpusitron.feature.officertask.tasks.domain

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
data class SubmittedLetter(
    val title: String,
    val number: String,
    val submissionDate: String,
    val requestedBy: String,
    val status: String,
    val id: Int,
    val isFinish: Boolean,
    val fileUrl: String?
): Parcelable


val SubmittedLetterType = object : NavType<SubmittedLetter>(
    isNullableAllowed = false
) {
    override fun put(bundle: Bundle, key: String, value: SubmittedLetter) {
        bundle.putParcelable(key, value)
    }
    override fun get(bundle: Bundle, key: String): SubmittedLetter {
        return bundle.getParcelable<SubmittedLetter>(key) as SubmittedLetter
    }

    override fun serializeAsValue(value: SubmittedLetter): String {
        // Serialized values must always be Uri encoded
        return Uri.encode(Json.encodeToString(value))
    }

    override fun parseValue(value: String): SubmittedLetter {
        // Navigation takes care of decoding the string
        // before passing it to parseValue()
        return Json.decodeFromString<SubmittedLetter>(value)
    }

}

internal val SubmittedLetterDummies = listOf(
    SubmittedLetter(
        title = "Pengajuan Surat Kehilangan",
        number = "P20240312001",
        submissionDate = "12 Mar 2024",
        requestedBy = "Sekda",
        status = "Ter TTE",
        id = 1,
        isFinish = false,
        fileUrl = null
    ),
    SubmittedLetter(
        title = "Pengajuan Surat Kehilangan",
        number = "P20240312001",
        submissionDate = "12 Mar 2024",
        requestedBy = "Sekda",
        status = "Ter TTE",
        id = 2,
        isFinish = true,
        fileUrl = ""
    ),
    SubmittedLetter(
        title = "Pengajuan Surat Kehilangan",
        number = "P20240312001",
        submissionDate = "12 Mar 2024",
        requestedBy = "Sekda",
        status = "Ter TTE",
        id = 3,
        isFinish = false,
        fileUrl = null
    )
)