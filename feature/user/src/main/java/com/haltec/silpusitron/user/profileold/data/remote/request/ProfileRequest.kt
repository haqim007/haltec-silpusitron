package com.haltec.silpusitron.user.profileold.data.remote.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileRequest(

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
	val marriageStatus: String
)
