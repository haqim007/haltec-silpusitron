package com.silpusitron.common.util

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDateTime
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale


fun LocalDateTime.toStringFormat() :String {
    val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale("id", "ID")) // Indonesian locale
    return this.toJavaLocalDateTime().format(formatter)
}

fun String.formatDate(): String {
    val date = LocalDate.parse(this)
    val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale("id", "ID"))
    return date.format(formatter)
}