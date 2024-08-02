package com.silpusitron.shared.form.domain.model

data class InputOptions(
	val options: List<InputTextData.Option>
)

fun InputOptions.getValue(label: String):String? {
	return this.options.firstOrNull { it.label == label }?.value
}


