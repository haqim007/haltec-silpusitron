package com.silpusitron.feature.auth.phonenumberupdate.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.silpusitron.core.ui.parts.getAppTextFieldColors
import com.silpusitron.data.mechanism.Resource
import com.silpusitron.feature.auth.R
import org.koin.androidx.compose.koinViewModel
import com.silpusitron.core.ui.R as CoreR


@Composable
fun PhoneNumberUpdateForm(
    modifier: Modifier = Modifier,
    viewModel: PhoneNumberUpdateViewModel = koinViewModel(),
    onClose: () -> Unit,
    onSubmitSucceed: () -> Unit
){

    val state by viewModel.state.collectAsState()
    val action = {action: UPNUiAction -> viewModel.doAction(action)}

    var phoneNumber by remember {
        mutableStateOf("")
    }

    LaunchedEffect(key1 = state.submitResult) {
        if (state.submitResult is Resource.Success){
            onSubmitSucceed()
            onClose()
        }
    }

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors().copy(
            containerColor = MaterialTheme.colorScheme.background
        )
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ){
            Text(
                text = stringResource(R.string.please_enter_valid_whatsapp_number),
                style = MaterialTheme.typography.labelMedium.copy(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = stringResource(R.string.input_whatsapp_number),
                style = MaterialTheme.typography.labelMedium.copy(
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )

            OutlinedTextField(
                value = phoneNumber,
                onValueChange = {
                    if (it.length < 15){
                        phoneNumber = it
                    }
                },
                colors = getAppTextFieldColors(),
                enabled = state.submitResult !is Resource.Loading,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            )

            Row(
                horizontalArrangement = if (state.submitResult !is Resource.Loading) {
                    Arrangement.spacedBy(8.dp)
                } else {
                    Arrangement.Center
                },
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 16.dp, bottom = 6.dp)
                    .fillMaxWidth()
            ) {
                if (state.submitResult !is Resource.Loading) {
                    Button(
                        modifier = Modifier
                            .weight(1f),
                        onClick = onClose,
                        colors = ButtonDefaults.buttonColors().copy(
                            containerColor = MaterialTheme.colorScheme.background,
                        ),
                        shape = RoundedCornerShape(5.dp),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                    ) {
                        Text(
                            text = stringResource(CoreR.string.back),
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Button(
                        modifier = Modifier
                            .weight(1f),
                        onClick = { action(UPNUiAction.Submit(phoneNumber)) },
                        colors = ButtonDefaults.buttonColors().copy(
                            containerColor = MaterialTheme.colorScheme.primary,
                            disabledContainerColor = MaterialTheme.colorScheme.background,
                            disabledContentColor = MaterialTheme.colorScheme.primary.copy(
                                alpha = 0.5f
                            ),
                        ),
                        shape = RoundedCornerShape(5.dp),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                        enabled = phoneNumber.isNotBlank() && (phoneNumber.length in 11..14)
                    ) {
                        Text(
                            text = stringResource(CoreR.string.save),
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else {
                    CircularProgressIndicator(
                        modifier = Modifier.size(30.dp),
                        strokeWidth = 3.dp
                    )
                }
            }
        }
    }

}