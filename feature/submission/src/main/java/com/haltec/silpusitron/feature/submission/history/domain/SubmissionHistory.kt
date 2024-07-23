package com.haltec.silpusitron.feature.submission.history.domain

data class SubmissionHistory(
    val title: String,
    val number: String,
    val submissionDate: String,
    val proceedBy: String,
    val status: String,
    val id: Int
)

val SubmissionHistoryDummies = listOf(
    SubmissionHistory(
        title = "Pengajuan Surat Kehilangan",
        number = "P20240312001",
        submissionDate = "12 Mar 2024",
        proceedBy = "Sekda",
        status = "Ter TTE",
        id = 1
    ),
    SubmissionHistory(
        title = "Pengajuan Surat Kehilangan",
        number = "P20240312001",
        submissionDate = "12 Mar 2024",
        proceedBy = "Sekda",
        status = "Ter TTE",
        id = 2
    ),
    SubmissionHistory(
        title = "Pengajuan Surat Kehilangan",
        number = "P20240312001",
        submissionDate = "12 Mar 2024",
        proceedBy = "Sekda",
        status = "Ter TTE",
        id = 3
    )
)