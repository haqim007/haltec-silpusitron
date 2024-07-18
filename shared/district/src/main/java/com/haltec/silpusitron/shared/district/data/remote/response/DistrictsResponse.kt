package com.haltec.silpusitron.shared.district.data.remote.response

import kotlinx.serialization.Serializable

@Serializable
data class DistrictsResponse(
	val data: List<DistrictResponse>
){

	@Serializable
	data class DistrictResponse(
		val kecamatan: String,
		val id: Int
	)
}


