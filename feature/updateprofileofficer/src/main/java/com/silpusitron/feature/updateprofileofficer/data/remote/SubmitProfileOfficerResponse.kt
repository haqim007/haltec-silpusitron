package com.silpusitron.feature.updateprofileofficer.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SubmitProfileOfficerResponse(
    val message: String,
    val errors: ProfileOfficerFieldsErrorResponse? = null,
)

@Serializable
data class ProfileOfficerFieldsErrorResponse(
    @SerialName("nik")
    val nik: String? = null,

    @SerialName("opd")
    val opd: String? = null,

    @SerialName("nama")
    val nama: String? = null,

    @SerialName("nip")
    val nip: String? = null,

    @SerialName("no_hp")
    val noHp: String? = null,

    @SerialName("email_gov")
    val emailGov: String? = null,

    @SerialName("email")
    val email: String? = null,

    @SerialName("status_pegawai")
    val statusPegawai: String? = null
)