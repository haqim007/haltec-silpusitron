package com.haltec.silpusitron.feature.dashboard.exposed.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NewsImagesResponse(

	@SerialName("data")
	val data: List<Item>
){
	@Serializable
	data class Item(

		@SerialName("image")
		val image: String,

		@SerialName("updated_at")
		val updatedAt: String,

		@SerialName("updated_by")
		val updatedBy: String,

		@SerialName("created_at")
		val createdAt: String,

		@SerialName("description")
		val description: String,

		@SerialName("active")
		val active: Int,

		@SerialName("id")
		val id: Int,

		@SerialName("title")
		val title: String,

		@SerialName("created_by")
		val createdBy: String
	)
}


