package com.haltec.silpusitron.feature.officertask.common.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.haltec.silpusitron.core.ui.R
import com.haltec.silpusitron.core.ui.component.LottieLoader
import com.haltec.silpusitron.core.ui.parts.SubmitSuccessView
import com.haltec.silpusitron.core.ui.parts.dialog.DialogError
import com.haltec.silpusitron.core.ui.parts.dialog.DialogLoadingDocView
import com.haltec.silpusitron.core.ui.parts.getAppTextFieldColors
import com.haltec.silpusitron.data.mechanism.Resource

@Composable
fun DialogSigningForm(
    onDismiss: () -> Unit,
    onSubmit: (passphrase: String) -> Unit
) {
    var passphrase by remember {
        mutableStateOf("")
    }
    Dialog(
        onDismissRequest = onDismiss
    ) {
        // Draw a rectangle shape with rounded corners inside the dialog
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(550.dp)
                .padding(2.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors().copy(
                containerColor = MaterialTheme.colorScheme.background
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
            ) {

                IconButton(
                    modifier = Modifier.align(Alignment.End),
                    onClick = onDismiss
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(id = R.string.cancel)
                    )
                }

                LottieLoader(
                    jsonRaw = R.raw.lottie_success_sparkle,
                    iterations = 3,
                    modifier = Modifier
                        .size(200.dp),
                    speed = 0.75f
                )

                Text(
                    text = stringResource(id = R.string.digital_signing),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                )

                Text(
                    text = stringResource(id = R.string.signing_confirmation),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 14.sp
                    )
                )

                var passwordHidden by remember {
                    mutableStateOf(true)
                }
                TextField(
                    value = passphrase,
                    onValueChange = { passphrase = it },
                    colors = getAppTextFieldColors(),
                    label = {
                        Text(text = stringResource(id = R.string.passphrase))
                    },
                    trailingIcon = {
                        IconButton(onClick = {
                            passwordHidden = !passwordHidden
                        }) {
                            val iconVisibility =
                                if (passwordHidden) Icons.Filled.VisibilityOff
                                else Icons.Filled.Visibility
                            val description =
                                if (passwordHidden)
                                    stringResource(R.string.show_password)
                                else
                                    stringResource(R.string.hide_password)
                            Icon(
                                imageVector = iconVisibility, contentDescription = description
                            )
                        }
                    },
                    visualTransformation = if (passwordHidden) PasswordVisualTransformation()
                    else VisualTransformation.None,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )

                Button(
                    onClick = {
                        onSubmit(passphrase)
                    },
                    shape = RoundedCornerShape(5.dp)
                ) {
                    Text(text = stringResource(id = R.string.confirm))
                }

            }
        }
    }
}
