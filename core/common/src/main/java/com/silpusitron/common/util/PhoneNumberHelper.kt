package com.silpusitron.common.util

fun String?.phoneNumberFormat() = this?.let {
    if (it.startsWith("62")){
        it.replaceFirst("^62".toRegex(), "")
    }
    else if (it.startsWith("08")){
        it.replaceFirst("^08".toRegex(), "8")
    }
    else (it)
} ?: run {
    ""
}