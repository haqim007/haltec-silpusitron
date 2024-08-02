package com.silpusitron.shared.form.domain.model

sealed class TextValidationType {
    data object Required : TextValidationType()
    data object Email : TextValidationType()
    data class MinLength(val minLength: Int) : TextValidationType()
    data class MaxLength(val maxLength: Int) : TextValidationType()
    data class ExactLength(val length: Int): TextValidationType()
    data class MinValue(val minValue: Number) : TextValidationType()
    data class MaxValue(val maxValue: Number) : TextValidationType()
    data object Invalid: TextValidationType()
}

