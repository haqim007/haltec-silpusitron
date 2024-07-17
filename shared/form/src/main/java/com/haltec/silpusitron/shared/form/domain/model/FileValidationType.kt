package com.haltec.silpusitron.shared.form.domain.model

sealed class FileValidationType {
    data object Required : FileValidationType()
    data class MaxSize(val limitInMB: Double) : FileValidationType()
}