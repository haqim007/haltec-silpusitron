package com.haltec.silpusitron.feature.officertask.common.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.haltec.silpusitron.core.ui.parts.SubmitSuccessView
import com.haltec.silpusitron.core.ui.parts.dialog.DialogError
import com.haltec.silpusitron.core.ui.parts.dialog.DialogLoadingDocView
import com.haltec.silpusitron.data.mechanism.Resource

@Composable
fun SigningResultView(
    signingResult: Resource<String>,
    onSuccess: () -> Unit,
    onDismiss: () -> Unit
) {
    when (signingResult) {
        is Resource.Error -> {
            DialogError(
                message = signingResult.message,
                onDismissRequest = onDismiss
            )
        }

        is Resource.Idle -> Unit
        is Resource.Loading -> {
            DialogLoadingDocView(
                onDismissRequest = {},
                properties = DialogProperties(
                    dismissOnClickOutside = false,
                    dismissOnBackPress = false
                )
            )
        }

        is Resource.Success -> {
            Dialog(
                onDismissRequest = {},
                properties = DialogProperties(
                    dismissOnBackPress = false,
                    dismissOnClickOutside = false
                )
            ) {
                SubmitSuccessView(onComplete = onSuccess)
            }
        }
    }
}