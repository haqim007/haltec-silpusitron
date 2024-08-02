package com.silpusitron.feature.officertask.tasks.data

import com.silpusitron.common.util.formatDate
import com.silpusitron.feature.officertask.tasks.domain.SubmittedLetter

internal fun SubmittedLettersResponse.toModels() = this.data.data.map {
    SubmittedLetter(
        id = it.id,
        title = it.templateSurat,
        number = it.noSurat,
        requestedBy = it.pemohon,
        status = it.statusLabel,
        submissionDate = it.tanggalPengajuan.formatDate(),
        isFinish = it.isFinish,
        fileUrl = it.url
    )
}