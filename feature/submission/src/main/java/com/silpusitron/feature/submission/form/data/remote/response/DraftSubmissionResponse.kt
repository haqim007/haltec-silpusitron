package com.silpusitron.feature.submission.form.data.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DraftSubmissionResponse(

	@SerialName("data")
	val data: Data
){

	@Serializable
	data class BerkasPersyaratanItem(

		@SerialName("path")
		val path: String,

		@SerialName("id")
		val id: String,

		@SerialName("label")
		val label: String,

		@SerialName("required")
		val required: Boolean
	)

	@Serializable
	data class Data(

		@SerialName("level")
		val level: String,

		@SerialName("jenis")
		val jenis: String,

		@SerialName("id")
		val id: Int,

		@SerialName("title")
		val title: String,

		@SerialName("formulir")
		val formulir: List<FormulirItem>,

		@SerialName("berkas_persyaratan")
		val berkasPersyaratan: List<BerkasPersyaratanItem>
	)

	@Serializable
	data class FormulirItem(

		@SerialName("default")
		val jsonMemberDefault: Boolean,

		@SerialName("label")
		val label: String,

		@SerialName("value")
		val value: String? = null,

		@SerialName("key")
		val key: String,

		@SerialName("required")
		val required: Boolean
	)
}

