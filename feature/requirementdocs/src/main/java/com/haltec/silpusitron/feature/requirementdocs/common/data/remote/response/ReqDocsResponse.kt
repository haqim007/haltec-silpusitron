package com.haltec.silpusitron.feature.requirementdocs.common.data.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class RequirementDocsResponse(

	@SerialName("data")
	val data: RequirementDocDataResponse
)

@Serializable
internal data class RequirementDocItemResponse(

	@SerialName("berkas_persyaratan_id")
	val requirementDocs: List<RequirementDocsItemResponse>? = null,

	@SerialName("jenis_surat")
	val jenisSurat: String,

	@SerialName("is_tt1")
	val isTt1: Int,

	@SerialName("is_tt2")
	val isTt2: Int,

	@SerialName("level")
	val level: String,

	@SerialName("level_surat")
	val levelSurat: String,

	@SerialName("penandaTangan")
	val signatoryResponse: SignatoryResponse,

	@SerialName("id")
	val id: Int,

	@SerialName("jenis_surat_id")
	val jenisSuratId: Int,

	@SerialName("title")
	val title: String,

	@SerialName("variable_id")
	val variable: List<VariableItem>? = null
)

@Serializable
internal data class VariableItem(

	@SerialName("default")
	val hide: Boolean,

	@SerialName("variable")
	val variable: String,

	@SerialName("id")
	val id: Int,

	@SerialName("key")
	val key: String,

	@SerialName("required")
	val required: Boolean
)

@Serializable
internal data class RequirementDocDataResponse(

	@SerialName("per_page")
	val perPage: Int,

	@SerialName("data")
	val data: List<RequirementDocItemResponse>,

	@SerialName("last_page")
	val lastPage: Int,

	@SerialName("next_page_url")
	val nextPageUrl: String?,

	@SerialName("prev_page_url")
	val prevPageUrl: String?,

	@SerialName("first_page_url")
	val firstPageUrl: String,

	@SerialName("path")
	val path: String,

	@SerialName("total")
	val total: Int,

	@SerialName("last_page_url")
	val lastPageUrl: String,

	@SerialName("from")
	val from: Int,

	@SerialName("links")
	val links: List<LinksItem>,

	@SerialName("to")
	val to: Int,

	@SerialName("current_page")
	val currentPage: Int
)

@Serializable
internal data class RequirementDocsItemResponse(

	@SerialName("id")
	val id: Int,

	@SerialName("berkas_persyaratan")
	val berkasPersyaratan: String,

	@SerialName("required")
	val required: Boolean
)

@Serializable
internal data class SignatoryResponse(

	@SerialName("camat")
	val camat: String? = null,

	@SerialName("plt_camat")
	val pltCamat: String? = null,

	@SerialName("sekretaris_kecamatan")
	val sekretarisKecamatan: String? = null,

	@SerialName("pelayanan_kecamatan")
	val pelayananKecamatan: String? = null,

	@SerialName("lurah")
	val lurah: String? = null,

	@SerialName("pelayanan_kelurahan")
	val pelayananKelurahan: String? = null,

	@SerialName("sekretaris_kelurahan")
	val sekretarisKelurahan: String? = null,

	@SerialName("plt_lurah")
	val pltLurah: String? = null
)

@Serializable
internal data class LinksItem(

	@SerialName("active")
	val active: Boolean,

	@SerialName("label")
	val label: String,

	@SerialName("url")
	val url: String?
)
