package com.haltec.silpusitron.user.profile.data.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject


@Serializable
data class ProfileResponse(
	val data: Data,
	@SerialName("message")
	val message: String? = null,
	@SerialName("errors")
	val errors: ProfileFieldsErrorResponse? = null
){

	@Serializable
	data class Data(

		@SerialName("hubungan_keluarga")
		val hubunganKeluarga: String?,

		@SerialName("desa_id")
		val desaId: Int?,

		@SerialName("rt")
		val rt: String?,

		@SerialName("no_hp")
		val noHp: String?,

		@SerialName("rw")
		val rw: String?,

		@SerialName("nama_ibu")
		val namaIbu: String?,

		@SerialName("golongan_darah")
		val golonganDarah: String?,

		@SerialName("nama_lengkap")
		val namaLengkap: String?,

		@SerialName("agama")
		val agama: String?,

		@SerialName("created_at")
		val createdAt: String,

		@SerialName("status_pernikahan_id")
		val statusPernikahanId: String?,

		@SerialName("bujur")
		val bujur: String?,

		@SerialName("nik")
		val nik: String,

		@SerialName("tempat_lahir")
		val tempatLahir: String?,

		@SerialName("hubungan_keluarga_id")
		val hubunganKeluargaId: String?,

		@SerialName("updated_at")
		val updatedAt: String,

		@SerialName("golongan_darah_id")
		val golonganDarahId: Int?,

		@SerialName("id")
		val id: Int,

		@SerialName("no_kk")
		val noKk: String,

		@SerialName("jenis_kelamin")
		val jenisKelamin: String?,

		@SerialName("tanggal_lahir")
		val tanggalLahir: String,

		@SerialName("desa")
		val desa: String?,

		@SerialName("pendidikan")
		val pendidikan: String?,

		@SerialName("lintang")
		val lintang: String?,

		@SerialName("active")
		val active: Int,

		@SerialName("jenis_kelamin_id")
		val jenisKelaminId: String?,

		@SerialName("agama_id")
		val agamaId: String?,

		@SerialName("kecamatan_id")
		val kecamatanId: Int?,

		@SerialName("created_by")
		val createdBy: String,

		@SerialName("alamat")
		val alamat: String?,

		@SerialName("pendidikan_id")
		val pendidikanId: String?,

		@SerialName("pekerjaan")
		val pekerjaan: String?,

		@SerialName("user_id")
		val userId: Int,

		@SerialName("kecamatan")
		val kecamatan: String?,

		@SerialName("nama_ayah")
		val namaAyah: String?,

		@SerialName("updated_by")
		val updatedBy: String?,

		@SerialName("pekerjaan_id")
		val pekerjaanId: String?,

		@SerialName("status_pernikahan")
		val statusPernikahan: String?
	)
}


