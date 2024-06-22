package com.haltec.silpusitron.core.domain.model


sealed class FileValidationType {
    data object Required : FileValidationType()
    data class MaxSize(val limitInMB: Double) : FileValidationType()
}