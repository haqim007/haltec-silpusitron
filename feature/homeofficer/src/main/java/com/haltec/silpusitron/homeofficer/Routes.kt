package com.haltec.silpusitron.homeofficer

import com.haltec.silpusitron.feature.officertask.tasks.domain.SubmittedLetter
import com.haltec.silpusitron.feature.submission.form.ui.SubmissionDocFormArgs
import com.haltec.silpusitron.feature.submissionhistory.common.domain.SubmissionHistory
import kotlinx.serialization.Serializable

internal sealed class Routes{
    @Serializable
    object DashboardRoute

    @Serializable
    object Tasks

    @Serializable
    object HistoriesRoute

    @Serializable
    object AccountRoute

    @Serializable
    object ProfileAccountRoute

    @Serializable
    data class DocPreviewApprovalRoute(
        val task: SubmittedLetter
    )

    @Serializable
    data class DocPreviewRoute(
        val history: SubmissionHistory
    )
}
