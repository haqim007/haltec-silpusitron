package com.silpusitron.feature.officertask.common.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.silpusitron.core.ui.parts.SubmitSuccessView
import com.silpusitron.core.ui.parts.dialog.DialogError
import com.silpusitron.core.ui.parts.dialog.DialogLoadingDocView
import com.silpusitron.data.mechanism.Resource

@Composable
fun SigningResultView(
    signingResult: Resource<String>,
    onSuccess: () -> Unit,
    onDismissError: () -> Unit
) {
    when (signingResult) {
        is Resource.Error -> {
            DialogError(
                message = signingResult.message,
                onDismissRequest = onDismissError
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