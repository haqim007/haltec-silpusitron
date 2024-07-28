package com.haltec.silpusitron.feature.updateprofileofficer.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ProfileOfficerResponse(

	@SerialName("data")
	val data: Data
){
	@Serializable
	data class Data(

		@SerialName("desa_id")
		val desaId: Int,

		@SerialName("desa")
		val desa: String,

		@SerialName("opd")
		val opd: String,

		@SerialName("no_hp")
		val noHp: String,

		@SerialName("finger_id")
		val fingerId: Int,

		@SerialName("jabatan")
		val jabatan: String,

		@SerialName("tipe_wilayah")
		val tipeWilayah: String,

		@SerialName("email_gov")
		val emailGov: String,

		@SerialName("kecamatan_id")
		val kecamatanId: Int,

		@SerialName("nama_wilayah")
		val namaWilayah: String,

		@SerialName("nik")
		val nik: String,

		@SerialName("wilayah_id")
		val wilayahId: Int,

		@SerialName("nip")
		val nip: String,

		@SerialName("nama")
		val nama: String,

		@SerialName("user_id")
		val userId: Int,

		@SerialName("kecamatan")
		val kecamatan: String,

		@SerialName("id")
		val id: Int,

		@SerialName("email")
		val email: String,

		@SerialName("status_pegawai")
		val statusPegawai: String
	)
}


