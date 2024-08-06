package com.silpusitron.core.ui.parts.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.silpusitron.core.ui.parts.loading.LoadingDocView

@Composable
fun DialogLoadingDocView(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    properties: DialogProperties = DialogProperties(),
){
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = properties
    ) {
        LoadingDocView(modifier)
    }
}