package com.haltec.silpusitron.feature.dashboard.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DashboardResponse(
	val data: Data? = null
){
	@Serializable
	data class Data(
		@SerialName("jenis_surat")
		val jenisSurat: List<JenisSuratItem>? = null,
		val statistik: List<StatistikItem>? = null
	)

	@Serializable
	data class StatistikItem(
		val label: String,
		val value: Int
	)

	@Serializable
	data class JenisSuratItem(
		val label: String,
		val value: Int
	)
}
