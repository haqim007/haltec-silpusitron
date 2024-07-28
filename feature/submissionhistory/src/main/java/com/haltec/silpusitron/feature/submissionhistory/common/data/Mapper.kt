package com.haltec.silpusitron.feature.submissionhistory.common.data

import com.haltec.silpusitron.common.util.formatDate
import com.haltec.silpusitron.feature.submissionhistory.common.data.remote.response.SubmissionHistoriesResponse
import com.haltec.silpusitron.feature.submissionhistory.common.domain.SubmissionHistory

internal fun SubmissionHistoriesResponse.toModels() = this.data.data.map {
    SubmissionHistory(
        id = it.id,
        title = it.templateSurat,
        number = it.noSurat,
        proceedBy = it.diprosesOleh,
        status = it.status,
        submissionDate = it.tanggalPengajuan.formatDate(),
        isFinish = it.isFinish,
        fileUrl = it.fileUrl
    )
}