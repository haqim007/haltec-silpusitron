package com.haltec.silpusitron.feature.submission.form.data.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TemplateResponse(
	@SerialName("data")
	val data: Data
){
	@Serializable
	data class Data(

        @SerialName("tt2_allow_edit")
		val tt2AllowEdit: Int,

        @SerialName("jenis_surat")
		val jenisSurat: JenisSurat,

        @SerialName("level")
		val level: String,

        @SerialName("tt1_petugas")
		val tt1Petugas: String,

        @SerialName("active")
		val active: Int,

        @SerialName("created_at")
		val createdAt: String,

        @SerialName("penandaTangan")
		val penandaTangan: PenandaTangan,

        @SerialName("tipe_wilayah_id")
		val tipeWilayahId: Int,

        @SerialName("tipe_wilayah")
		val tipeWilayah: TipeWilayah,

        @SerialName("created_by")
		val createdBy: String,

        @SerialName("berkas_persyaratan_id")
		val berkasPersyaratanId: List<BerkasPersyaratanIdItem>? = null,

        @SerialName("is_tt1")
		val isTt1: Int,

        @SerialName("tt1_allow_edit")
		val tt1AllowEdit: Int,

        @SerialName("is_tt2")
		val isTt2: Int,

        @SerialName("nama")
		val nama: String,

        @SerialName("tt2_petugas")
		val tt2Petugas: String? = null,

        @SerialName("updated_at")
		val updatedAt: String? = null,

        @SerialName("variable_id")
		val variableId: List<VariableIdItem>? = null,

        @SerialName("konten")
		val konten: String,

        @SerialName("level_surat")
		val levelSurat: String,

        @SerialName("updated_by")
		val updatedBy: String? = null,

        @SerialName("id")
		val id: Int,

        @SerialName("jenis_surat_id")
		val jenisSuratId: Int
	)


	@Serializable
	data class TipeWilayah(

		@SerialName("updated_at")
		val updatedAt: String,

		@SerialName("updated_by")
		val updatedBy: String? = null,

		@SerialName("active")
		val active: Int,

		@SerialName("created_at")
		val createdAt: String,

		@SerialName("id")
		val id: Int,

		@SerialName("tipe_wilayah")
		val tipeWilayah: String,

		@SerialName("created_by")
		val createdBy: Int
	)

	@Serializable
	data class JenisSurat(

		@SerialName("jenis_surat")
		val jenisSurat: String,

		@SerialName("updated_at")
		val updatedAt: String,

		@SerialName("updated_by")
		val updatedBy: Int,

		@SerialName("active")
		val active: Int,

		@SerialName("created_at")
		val createdAt: String,

		@SerialName("id")
		val id: Int,

		@SerialName("created_by")
		val createdBy: Int
	)

	@Serializable
	data class PenandaTangan(

		@SerialName("lurah")
		val lurah: String? = null,

		@SerialName("pelayanan_kelurahan")
		val pelayananKelurahan: String? = null,

		@SerialName("sekretaris_kelurahan")
		val sekretarisKelurahan: String? = null,

		@SerialName("plt_lurah")
		val pltLurah: String? = null,

		@SerialName("camat")
		val camat: String? = null,

		@SerialName("plt_camat")
		val pltCamat: String? = null,

		@SerialName("pelayanan_kecamatan")
		val pelayananKecamatan: String? = null,

		@SerialName("sekretaris_kecamatan")
		val sekretarisKecamatan: String? = null,
	)
	@Serializable
	data class BerkasPersyaratanIdItem(

		@SerialName("id")
		val id: Int,

		@SerialName("berkas_persyaratan")
		val berkasPersyaratan: String,

		@SerialName("required")
		val required: Boolean
	)
	@Serializable
	data class VariableIdItem(

		@SerialName("default")
		val jsonMemberDefault: Boolean,

		@SerialName("variable")
		val variable: String,

		@SerialName("id")
		val id: Int,

		@SerialName("key")
		val key: String,

		@SerialName("required")
		val required: Boolean
	)

}

