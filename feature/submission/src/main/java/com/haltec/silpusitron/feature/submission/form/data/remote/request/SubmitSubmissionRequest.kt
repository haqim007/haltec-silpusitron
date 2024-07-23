package com.haltec.silpusitron.feature.submission.form.data.remote.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SubmitSubmissionRequest(
	@SerialName("template_surat_id")
	val tempalateSuratId: Int,

	@SerialName("desa_id")
	val subDistrictId: String,

	@SerialName("rt")
	val rt: String,

	@SerialName("no_hp")
	val phoneNumber: String,

	@SerialName("pendidikan")
	val educationId: String,

	@SerialName("rw")
	val rw: String,

	@SerialName("nama_ibu")
	val motherName: String,

	@SerialName("golongan_darah")
	val bloodTypeId: String,

	@SerialName("nama_lengkap")
	val fullname: String,

	@SerialName("agama")
	val religionId: String,

	@SerialName("kecamatan_id")
	val districtId: String,

	@SerialName("alamat")
	val address: String,

	@SerialName("nik")
	val idNumber: String,

	@SerialName("tempat_lahir")
	val birthPlace: String,

	@SerialName("pekerjaan")
	val profession: String,

	@SerialName("status_hubkel")
	val famRelationId: String,

	@SerialName("nama_ayah")
	val fatherName: String,

	@SerialName("no_kk")
	val famCardNumber: String,

	@SerialName("jenis_kelamin")
	val genderId: String,

	@SerialName("tanggal_lahir")
	val birthDate: String,

	@SerialName("status_pernikahan")
	val marriageStatus: String,

	@SerialName("lintang")
	val latitude: String,

	@SerialName("bujur")
	val longitude: String,

	@SerialName("berkas_persyaratan")
	val docs: List<BerkasPersyaratanItem>,

	@SerialName("formulir")
	val forms: List<FormulirItem>
){
	@Serializable
	data class FormulirItem(
		val id: Int,
		val value: String
	)

	@Serializable
	data class BerkasPersyaratanItem(
		val id: Int,
//		val value: ByteArray
		val file: FileRequest
	)

	@Serializable
	data class FileRequest(
		val name: String,
		val mimeType: String,
		val binary: ByteArray
	) {
		override fun equals(other: Any?): Boolean {
			if (this === other) return true
			if (javaClass != other?.javaClass) return false

			other as FileRequest

			if (name != other.name) return false
			if (mimeType != other.mimeType) return false
			if (!binary.contentEquals(other.binary)) return false

			return true
		}

		override fun hashCode(): Int {
			var result = name.hashCode()
			result = 31 * result + mimeType.hashCode()
			result = 31 * result + binary.contentHashCode()
			return result
		}
	}

	data class Profile(
		val subDistrictId: String,
		val rt: String,
		val phoneNumber: String,
		val educationId: String,
		val rw: String,
		val motherName: String,
		val bloodTypeId: String,
		val fullname: String,
		val religionId: String,
		val districtId: String,
		val address: String,
		val idNumber: String,
		val birthPlace: String,
		val profession: String,
		val famRelationId: String,
		val fatherName: String,
		val famCardNumber: String,
		val genderId: String,
		val birthDate: String,
		val marriageStatus: String,
		val latitude: String,
		val longitude: String
	)

}
