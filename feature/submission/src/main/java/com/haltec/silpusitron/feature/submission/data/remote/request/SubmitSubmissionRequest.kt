package com.haltec.silpusitron.feature.submission.data.remote.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SubmitSubmissionRequest(

	@SerialName("data")
	val data: Data
){
	@Serializable
	data class FormulirItem(

		@SerialName("id")
		val id: Int,

		@SerialName("value")
		val value: String
	)

	@Serializable
	data class BerkasPersyaratanItem(

		@SerialName("id")
		val id: Int,

		@SerialName("value")
		val value: String
	)

	@Serializable
	data class Data(

		@SerialName("desa_id")
		val desaId: Int,

		@SerialName("rt")
		val rt: String,

		@SerialName("no_hp")
		val noHp: String,

		@SerialName("rw")
		val rw: String,

		@SerialName("nama_ibu")
		val namaIbu: String,

		@SerialName("lintang")
		val lintang: String,

		@SerialName("status_pernikahan_id")
		val statusPernikahanId: Int,

		@SerialName("jenis_kelamin_id")
		val jenisKelaminId: Int,

		@SerialName("agama_id")
		val agamaId: String,

		@SerialName("kecamatan_id")
		val kecamatanId: Int,

		@SerialName("berkas_persyaratan")
		val berkasPersyaratan: List<BerkasPersyaratanItem>,

		@SerialName("formulir")
		val formulir: List<FormulirItem>,

		@SerialName("alamat")
		val alamat: String,

		@SerialName("bujur")
		val bujur: String,

		@SerialName("pendidikan_id")
		val pendidikanId: Int,

		@SerialName("tempat_lahir")
		val tempatLahir: String,

		@SerialName("hubungan_keluarga_id")
		val hubunganKeluargaId: Int,

		@SerialName("nama_ayah")
		val namaAyah: String,

		@SerialName("template_surat_id")
		val templateSuratId: Int,

		@SerialName("golongan_darah_id")
		val golonganDarahId: Int,

		@SerialName("pekerjaan_id")
		val pekerjaanId: Int
	)

}
