package com.silpusitron.shared.formprofile.data.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class SubDistrictsResponse(
	val data: List<SubDistrictResponse>
){
	@Serializable
	internal data class SubDistrictResponse(
		val desa: String,
		val id: Int,
		@SerialName("kecamatan_id")
		val kecamatanId: Int
	)

}


