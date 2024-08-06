package com.silpusitron.core.ui.parts.dialog

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.silpusitron.core.ui.R
import com.silpusitron.core.ui.parts.error.ErrorView

@Composable
fun DialogError(
    message: String?,
    properties: DialogProperties = DialogProperties(),
    onDismissRequest: () -> Unit,
    tryAgain: Boolean = true,
    onTryAgain: (() -> Unit)? = null
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = properties
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(450.dp)
                .padding(2.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors().copy(
                containerColor = MaterialTheme.colorScheme.background
            )
        ) {
            Box {
                ErrorView(
                    modifier = Modifier,
                    message = message,
                    onTryAgain = onTryAgain
                )

                IconButton(
                    onClick = onDismissRequest,
                    modifier = Modifier.align(Alignment.TopEnd),
                    colors = IconButtonDefaults.iconButtonColors().copy(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = stringResource(id = R.string.close)
                    )
                }
            }
        }
    }
}