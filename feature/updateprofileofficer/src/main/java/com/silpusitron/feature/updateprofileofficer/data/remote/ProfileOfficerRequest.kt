package com.silpusitron.feature.updateprofileofficer.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileOfficerRequest(

	@SerialName("data")
	val data: Data
){

	@Serializable
	data class Data(

		@SerialName("nik")
		val nik: String,

		@SerialName("opd")
		val opd: String,

		@SerialName("nama")
		val nama: String,

		@SerialName("nip")
		val nip: String,

		@SerialName("no_hp")
		val noHp: String,

		@SerialName("email_gov")
		val emailGov: String,

		@SerialName("email")
		val email: String,

		@SerialName("status_pegawai")
		val statusPegawai: String
	)
}


