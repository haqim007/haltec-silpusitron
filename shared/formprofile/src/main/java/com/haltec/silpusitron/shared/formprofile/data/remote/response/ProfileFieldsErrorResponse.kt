package com.haltec.silpusitron.shared.formprofile.data.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileFieldsErrorResponse(

    @SerialName("desa_id")
    val desaId: String? = null,

    @SerialName("rt")
    val rt: String? = null,

    @SerialName("pendidikan")
    val pendidikan: String? = null,

    @SerialName("no_hp")
    val noHp: String? = null,

    @SerialName("nama_ibu")
    val namaIbu: String? = null,

    @SerialName("rw")
    val rw: String? = null,

    @SerialName("lintang")
    val lintang: String? = null,

    @SerialName("golongan_darah")
    val golonganDarah: String? = null,

    @SerialName("agama")
    val agama: String? = null,

    @SerialName("kecamatan_id")
    val kecamatanId: String? = null,

    @SerialName("alamat")
    val alamat: String? = null,

    @SerialName("bujur")
    val bujur: String? = null,

    @SerialName("tempat_lahir")
    val tempatLahir: String? = null,

    @SerialName("pekerjaan")
    val pekerjaan: String? = null,

    @SerialName("status_hubkel")
    val statusHubkel: String? = null,

    @SerialName("nama_ayah")
    val namaAyah: String? = null,

    @SerialName("jenis_kelamin")
    val jenisKelamin: String? = null,

    @SerialName("status_pernikahan")
    val statusPernikahan: String? = null,

    @SerialName("nik")
    val nik: String? = null
)