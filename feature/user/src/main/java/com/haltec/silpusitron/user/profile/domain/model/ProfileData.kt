package com.haltec.silpusitron.user.profile.domain.model

import com.haltec.silpusitron.core.domain.model.InputTextData
import com.haltec.silpusitron.core.domain.model.TextValidationType
import com.haltec.silpusitron.user.profile.ui.formProfileStateDummy

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
		input = formProfileStateDummy.inputs
	)
