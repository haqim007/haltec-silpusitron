package com.haltec.silpusitron.core.ui.parts

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun DialogLoadingDocView(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    properties: DialogProperties = DialogProperties(),
){
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = properties
//        properties = DialogProperties(
//            dismissOnClickOutside = false,
//            dismissOnBackPress = false
//        )
    ) {
        LoadingDocView(modifier)
    }
}