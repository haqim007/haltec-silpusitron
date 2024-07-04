package com.haltec.silpusitron.user.profile.data.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SubDistrictsResponse(
	val data: List<SubDistrictResponse>
){
	@Serializable
	data class SubDistrictResponse(
		val desa: String,
		val id: Int,
		@SerialName("kecamatan_id")
		val kecamatanId: Int
	)

}


