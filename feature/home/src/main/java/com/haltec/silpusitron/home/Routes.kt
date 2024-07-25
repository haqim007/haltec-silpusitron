package com.haltec.silpusitron.home

import com.haltec.silpusitron.feature.submission.form.ui.SubmissionDocFormArgs
import com.haltec.silpusitron.feature.submission.history.domain.SubmissionHistory
import kotlinx.serialization.Serializable

@Serializable
object DashboardRoute

@Serializable
object InquiryRoute

@Serializable
object HistoriesRoute

@Serializable
object AccountRoute

@Serializable
object ProfileAccountRoute

@Serializable
data class FormSubmission(
    val args: SubmissionDocFormArgs
)

@Serializable
data class DocPreviewRoute(
    val history: SubmissionHistory
)