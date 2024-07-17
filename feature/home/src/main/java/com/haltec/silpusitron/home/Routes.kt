package com.haltec.silpusitron.home

import com.haltec.silpusitron.feature.submission.ui.SubmissionDocFormArgs
import kotlinx.serialization.Serializable

@Serializable
object DashboardRoute

@Serializable
object InquiryRoute

@Serializable
object HistoriesRoute

@Serializable
object ProfileRoute

@Serializable
data class FormSubmission(
    val args: SubmissionDocFormArgs
)