package com.haltec.silpusitron.shared.formprofile.domain.model

import com.haltec.silpusitron.shared.form.domain.model.InputTextData
import com.haltec.silpusitron.shared.form.domain.model.TextValidationType
import com.haltec.silpusitron.shared.formprofile.data.dummy.formProfileInputDummy

data class ProfileData(
	val id: Int,
	val active: Boolean,
	val userId: Int,
	val input: Map<FormProfileInputKey, InputTextData<TextValidationType, String>>
)

val ProfileDataDummy: ProfileData
	get() = ProfileData(
		0,
		false,
		0,
		input = formProfileInputDummy
	)
