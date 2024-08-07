package com.silpusitron.feature.auth.otp.ui

import android.app.Activity
import android.text.TextPaint
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withAnnotation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.silpusitron.common.di.commonModule
import com.silpusitron.core.ui.parts.ContainerWithBanner
import com.silpusitron.core.ui.theme.BackgroundLight
import com.silpusitron.core.ui.theme.SILPUSITRONTheme
import com.silpusitron.data.mechanism.Resource
import com.silpusitron.feature.auth.R
import com.silpusitron.feature.auth.di.authModule
import com.silpusitron.feature.auth.phonenumberupdate.ui.PhoneNumberUpdateForm
import com.silpusitron.shared.form.ui.components.ErrorValidationText
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.KoinApplication
import com.silpusitron.core.ui.R as CoreR

@Composable
fun OTPScreen(
    modifier: Modifier = Modifier
){
    ContainerWithBanner(
        modifier = modifier
            .fillMaxSize(),
        bannerModifier = Modifier
            .height(242.dp),
        sharedModifier = Modifier
    ) {
        Card(
            modifier = Modifier
                .padding(
                    start = 32.dp,
                    end = 32.dp,
                    top = 50.dp,
                    bottom = 50.dp,
                )
                .defaultMinSize(
                    minHeight = 251.dp
                )
                .fillMaxWidth(),
            colors = CardDefaults.cardColors().copy(
                containerColor = BackgroundLight
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 10.dp
            )
        ) {

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                OTPForm()
            }
        }
    }
}

@Composable
private fun OTPForm(
    modifier: Modifier = Modifier,
    viewModel: OTPViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsState()
    val action = {action: OTPUiAction -> viewModel.doAction(action)}

    if (state.isLoading) CircularProgressIndicator()

    var showPhoneNumberForm by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = Unit){
        action(OTPUiAction.RequestOTP)
    }

    if (state.otpVerificationResult is Resource.Error){
        AlertDialog(
            onDismissRequest = { action(OTPUiAction.Retry) },
            confirmButton = {
                TextButton(onClick = { action(OTPUiAction.Retry) }) {
                    Text(stringResource(CoreR.string.ok))
                }
            },
            icon = {
                Icon(Icons.Default.Warning, contentDescription = null)
            },
            title = {
                Text(text = stringResource(CoreR.string.attention_))
            },
            text = {
                Text(text = state.otpVerificationResult.message ?: stringResource(R.string.failed_to_verify_otp))
            }
        )
    }

    val context = LocalContext.current
    if (state.requestOTPResult is Resource.Error){
        AlertDialog(
            onDismissRequest = { action(OTPUiAction.RequestOTP) },
            confirmButton = {
                TextButton(
                    onClick = {
                        action(OTPUiAction.RequestOTP)
                    }
                ) {
                    Text(stringResource(CoreR.string.try_again))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        (context as? Activity)?.finish()
                    }
                ) {
                    Text(stringResource(CoreR.string.close_app))
                }
            },
            icon = {
                Icon(Icons.Default.Warning, contentDescription = null)
            },
            title = {
                Text(text = stringResource(CoreR.string.attention_))
            },
            text = {
                Text(text = state.requestOTPResult.message ?: stringResource(R.string.failed_to_retreive_otp))
            }
        )
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        if (state.requestOTPResult is Resource.Success){
            Text(
                text = stringResource(R.string.please_check_otp_code),
                style = MaterialTheme.typography.labelLarge.copy(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(16.dp)
            )
        }

        Text(
            text = stringResource(R.string.input_otp_code),
            style = MaterialTheme.typography.labelLarge.copy(
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.primary
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .width(200.dp)
                .padding(10.dp)
        )

        Text(
            text = state.otpTimeOutInString ?: "0:00",
            style = MaterialTheme.typography.labelLarge.copy(
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.primary
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .width(200.dp)
                .padding(4.dp)

        )

        val color = if (state.otpInput.isValid)
            MaterialTheme.colorScheme.primary
        else
            MaterialTheme.colorScheme.error

        val maxLength = 6
        BasicTextField(
            value = state.otp,
            onValueChange = {
                if (it.length <= maxLength) {
                    action(OTPUiAction.SetOTP(it))
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.NumberPassword,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    if (state.enableToVerifyOTP) action(OTPUiAction.Verify)
                }
            ),
            singleLine = true,
            modifier = Modifier.padding(top = 16.dp),
            enabled = !state.isLoading,

        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                repeat(maxLength) { index ->
                    val number: String = when {
                        index >= state.otp.length -> ""
                        else -> state.otp[index].toString()
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = number,
                            style = MaterialTheme.typography.titleLarge.copy(
                                color = color
                            )
                        )

                        Box(
                            modifier = Modifier
                                .width(40.dp)
                                .height(5.dp)
                                .background(color)
                        )
                    }
                }
            }
        }

        ErrorValidationText(
            data = state.otpInput, labelName = "OTP",
            modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 16.dp)
        )

        if (state.requestOTPResult is Resource.Success){
            val note = stringResource(
                R.string.otp_code_has_been_sent,
                state.requestOTPResult.data?.phoneNumber ?: ""
            )
            val changePhoneNumber = " ${stringResource(R.string.update_phone_number_question)}"
            val annotatedString = buildAnnotatedString {
                pushStyle(SpanStyle(
                    fontWeight = FontWeight.SemiBold,
                    fontStyle = FontStyle.Italic
                ))
                append(note)
                pushStyle(SpanStyle(color = MaterialTheme.colorScheme.primary)) // Set bold style
                append(changePhoneNumber)
                pop()
            }

            var changePhoneNumberOffset: Offset? = null
            BasicText(
                text = annotatedString,
                modifier = Modifier
                    .padding(16.dp)
                    .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        val changePhoneNumberOffsetY = (changePhoneNumberOffset?.y ?: 0f)
                        if (offset.y in changePhoneNumberOffsetY..(changePhoneNumberOffsetY + 35f)){
                            showPhoneNumberForm = true
                        }
                    }
                },
                onTextLayout = { textLayoutResult ->
                    changePhoneNumberOffset = getClickableArea(
                        textLayoutResult,
                        note.length-1,
                        note.length + changePhoneNumber.length - 1
                    )
                }
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp, bottom = 6.dp)
        ) {
            Button(
                modifier = Modifier
                    .weight(1f),
                onClick = { action(OTPUiAction.RequestOTP) },
                colors = ButtonDefaults.buttonColors().copy(
                    containerColor = MaterialTheme.colorScheme.primary,
                    disabledContainerColor = MaterialTheme.colorScheme.background,
                    disabledContentColor = MaterialTheme.colorScheme.primary,
                ),
                shape = RoundedCornerShape(5.dp),
                enabled = state.enableRequestOTPAgain,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
            ) {
                Text(
                    text = stringResource(R.string.resent),
                    fontWeight = FontWeight.Bold
                )
            }

            Button(
                modifier = Modifier
                    .weight(1f),
                onClick = { action(OTPUiAction.Verify) },
                colors = ButtonDefaults.buttonColors().copy(
                    containerColor = MaterialTheme.colorScheme.primary,
                    disabledContainerColor = MaterialTheme.colorScheme.background,
                    disabledContentColor = MaterialTheme.colorScheme.primary,
                ),
                shape = RoundedCornerShape(5.dp),
                enabled = state.enableToVerifyOTP,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
            ) {
                Text(
                    text = stringResource(R.string.verify_otp),
                    fontWeight = FontWeight.Bold
                )
            }
        }

        if (showPhoneNumberForm){
            Dialog(
                onDismissRequest = { showPhoneNumberForm = false }
            ) {
                PhoneNumberUpdateForm(
                    onClose = { showPhoneNumberForm = false },
                    onSubmitSucceed = {
                        action(OTPUiAction.RequestOTP)
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OTPScreenPreview() {
    KoinApplication(application = {
        modules(listOf(commonModule, authModule))
    }) {
        SILPUSITRONTheme {
            OTPScreen()
        }
    }
}

fun getClickableArea(
    textLayout: TextLayoutResult,
    startOffset: Int,
    endOffset: Int
): Offset? {
    val textOffset = textLayout.getOffsetForPosition(Offset(startOffset.toFloat(), endOffset.toFloat()))
    return textLayout.getBoundingBox(textOffset).let {
        if (it.isEmpty) null else it.topRight
    }
}
