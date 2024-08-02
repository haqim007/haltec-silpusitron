package com.silpusitron.feature.submission.form.ui

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
data class SubmissionDocFormArgs(
    val id: Int,
    val title: String,
    val letterLevel: String,
    val letterType: String
): Parcelable

val submissionDocFormArgsDummy = SubmissionDocFormArgs(
    id = 1,
    title = "Test",
    letterLevel = "Kecamatan",
    letterType = "Resmi"
)


val SubmissionDocFormArgsType = object : NavType<SubmissionDocFormArgs>(
    isNullableAllowed = false
) {
    override fun put(bundle: Bundle, key: String, value: SubmissionDocFormArgs) {
        bundle.putParcelable(key, value)
    }
    override fun get(bundle: Bundle, key: String): SubmissionDocFormArgs {
        return bundle.getParcelable<SubmissionDocFormArgs>(key) as SubmissionDocFormArgs
    }

    override fun serializeAsValue(value: SubmissionDocFormArgs): String {
        // Serialized values must always be Uri encoded
        return Uri.encode(Json.encodeToString(value))
    }

    override fun parseValue(value: String): SubmissionDocFormArgs {
        // Navigation takes care of decoding the string
        // before passing it to parseValue()
        return Json.decodeFromString<SubmissionDocFormArgs>(value)
    }

}