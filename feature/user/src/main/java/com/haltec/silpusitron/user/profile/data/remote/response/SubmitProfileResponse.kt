package com.haltec.silpusitron.user.profile.data.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SubmitProfileResponse(
    val message: String,
    val errors: Errors? = null,
){
    @Serializable
    data class Errors(

        @SerialName("desa_id")
        val desaId: String?,

        @SerialName("rt")
        val rt: String?,

        @SerialName("pendidikan")
        val pendidikan: String?,

        @SerialName("no_hp")
        val noHp: String?,

        @SerialName("nama_ibu")
        val namaIbu: String?,

        @SerialName("rw")
        val rw: String?,

        @SerialName("lintang")
        val lintang: String?,

        @SerialName("golongan_darah")
        val golonganDarah: String?,

        @SerialName("agama")
        val agama: String?,

        @SerialName("kecamatan_id")
        val kecamatanId: String?,

        @SerialName("alamat")
        val alamat: String?,

        @SerialName("bujur")
        val bujur: String?,

        @SerialName("tempat_lahir")
        val tempatLahir: String?,

        @SerialName("pekerjaan")
        val pekerjaan: String?,

        @SerialName("status_hubkel")
        val statusHubkel: String?,

        @SerialName("nama_ayah")
        val namaAyah: String?,

        @SerialName("jenis_kelamin")
        val jenisKelamin: String?,

        @SerialName("status_pernikahan")
        val statusPernikahan: String?
    )
}
