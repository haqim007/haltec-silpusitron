package com.silpusitron.feature.officertask.tasks.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
internal data class SubmittedLettersResponse(

	@SerialName("data")
	val data: Data
){

	@Serializable
	data class LinksItem(

		@SerialName("active")
		val active: Boolean,

		@SerialName("label")
		val label: String,

		@SerialName("url")
		val url: String? = null
	)


	@Serializable
	data class DataItem(

		@SerialName("status_label")
		val statusLabel: String,

		@SerialName("jenis_surat")
		val jenisSurat: String,

		@SerialName("diproses_oleh")
		val diprosesOleh: String,

		@SerialName("pemohon")
		val pemohon: String,

		@SerialName("no_surat")
		val noSurat: String,

		@SerialName("is_finish")
		val isFinish: Boolean,

		@SerialName("id")
		val id: Int,

		@SerialName("tanggal_pengajuan")
		val tanggalPengajuan: String,

		@SerialName("url")
		val url: String,

		@SerialName("template_surat")
		val templateSurat: String,

		@SerialName("status")
		val status: String
	)

	@Serializable
	data class Data(

        @SerialName("per_page")
		val perPage: Int,

        @SerialName("data")
		val data: List<DataItem>,

        @SerialName("last_page")
		val lastPage: Int,

        @SerialName("next_page_url")
		val nextPageUrl: String? = null,

        @SerialName("prev_page_url")
		val prevPageUrl: String? = null,

        @SerialName("first_page_url")
		val firstPageUrl: String,

        @SerialName("path")
		val path: String,

        @SerialName("total")
		val total: Int,

        @SerialName("last_page_url")
		val lastPageUrl: String,

        @SerialName("from")
		val from: Int? = null,

        @SerialName("links")
		val links: List<LinksItem>,

        @SerialName("to")
		val to: Int? = null,

        @SerialName("current_page")
		val currentPage: Int
	)

}

