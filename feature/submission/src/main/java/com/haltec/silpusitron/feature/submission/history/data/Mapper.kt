package com.haltec.silpusitron.feature.submission.history.data

import com.haltec.silpusitron.feature.submission.history.data.remote.response.SubmissionHistoriesResponse
import com.haltec.silpusitron.feature.submission.history.domain.SubmissionHistory

fun SubmissionHistoriesResponse.toModels() = this.data.data.map {
    SubmissionHistory(
        id = it.id,
        title = it.templateSurat,
        number = it.noSurat,
        proceedBy = it.diprosesOleh,
        status = it.status,
        submissionDate = it.tanggalPengajuan
    )
}