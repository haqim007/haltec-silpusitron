package com.silpusitron.feature.submissionhistory.common.data

import com.silpusitron.common.util.formatDate
import com.silpusitron.feature.submissionhistory.common.data.remote.response.SubmissionHistoriesResponse
import com.silpusitron.feature.submissionhistory.common.domain.SubmissionHistory

internal fun SubmissionHistoriesResponse.toModels() = this.data.data.map {
    SubmissionHistory(
        id = it.id,
        title = it.templateSurat,
        number = it.noSurat,
        proceedBy = it.diprosesOleh,
        statusLabel = it.statusLabel,
        submissionDate = it.tanggalPengajuan.formatDate(),
        isFinish = it.isFinish,
        fileUrl = it.fileUrl,
        status = it.status,
        letterType = it.jenisSurat,
        isEditable = it.isEditable
    )
}