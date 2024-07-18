package com.haltec.silpusitron.feature.dashboard.common.data.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DashboardResponse(
    val data: Data? = null,
    val message: String? = null
){
	@Serializable
	data class Data(
        @SerialName("jenis_surat")
		val jenisSurat: List<PieDataResponse>? = null,
        val statistik: List<PieDataResponse>? = null,
        @SerialName("rasio_pelayanan")
		val rasioPelayanan: List<RasioPelayananResponse>? = null,
        @SerialName("status_surat")
		val statusSurat: List<PieDataResponse>? = null
	)

	@Serializable
	data class PieDataResponse(
		val label: String,
		@SerialName("jumlah")
		val value: Int
	)

	@Serializable
	data class RasioPelayananResponse(
		val label: String,
		@SerialName("surat_masuk")
		val totalLetterIn: Int,
		@SerialName("surat_keluar")
		val totalLetterOut: Int
	)

	@Serializable
	data class StatistikItem(
		val label: String,
		@SerialName("jumlah")
		val value: Int
	)

	@Serializable
	data class JenisSuratItem(
		val label: String,
		@SerialName("jumlah")
		val value: Int
	)
}
