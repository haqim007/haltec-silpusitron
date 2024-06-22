package com.haltec.silpusitron.core.domain.model

data class ErrorFieldInfo<T> (
    val code: T? = null,
    val message: String
)