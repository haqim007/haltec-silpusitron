package com.haltec.silpusitron.feature.auth.login.ui

import android.app.Activity
import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.recaptcha.Recaptcha
import com.google.android.recaptcha.RecaptchaAction
import com.google.android.recaptcha.RecaptchaClient
import com.haltec.silpusitron.common.di.commonModule
import com.haltec.silpusitron.core.ui.component.FormTextField
import com.haltec.silpusitron.core.ui.parts.ErrorValidationText
import com.haltec.silpusitron.core.ui.theme.BackgroundLight
import com.haltec.silpusitron.core.ui.theme.SILPUSITRONTheme
import com.haltec.silpusitron.data.mechanism.Resource
import com.haltec.silpusitron.feature.auth.BuildConfig
import com.haltec.silpusitron.feature.auth.R
import com.haltec.silpusitron.feature.auth.common.domain.UserType
import com.haltec.silpusitron.feature.auth.di.authModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.KoinApplication
import com.haltec.silpusitron.core.ui.R as CoreR

@Composable
fun LoginForm(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = koinViewModel(),
    userType: UserType,
    onLoginSuccess: () -> Unit
) {
    var recaptchaClient: RecaptchaClient? by remember {
        mutableStateOf(null)
    }
    val state = viewModel.state.collectAsState()
    val action = {action: LoginUiAction -> viewModel.doAction(action)}
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val application = context.applicationContext as? Application
    var initRecaptcha: Boolean? by rememberSaveable {
        mutableStateOf(null)
    }
    val isLoading = recaptchaClient == null || state.value.loginResult is Resource.Loading
    var showLoginErrorAlert by rememberSaveable {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = initRecaptcha) {
        if (initRecaptcha == null){
            if (application != null) {
                Recaptcha.getClient(application, BuildConfig.RECAPTCHA_KEY_ID)
                    .onSuccess {
                        recaptchaClient = it
                        initRecaptcha = true
                        Log.d("recaptchaClient", it.toString())
                    }
                    .onFailure {
                        initRecaptcha = false
                        Log.d("recaptchaClient", it.localizedMessage ?: "error")
                    }
            }
        }
    }

    LaunchedEffect(key1 = state.value.loginResult) {
        state.value.loginResult.let {
            showLoginErrorAlert = false
            if (it is Resource.Success){
                onLoginSuccess()
            }
            else if(it is Resource.Error){
                showLoginErrorAlert = true
            }
        }
    }

    if (showLoginErrorAlert){
        AlertDialog(
            onDismissRequest = { showLoginErrorAlert = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        showLoginErrorAlert = false
                    }
                ) {
                    Text(stringResource(R.string.ok))
                }
            },
            icon = {
                Icon(Icons.Default.Warning, contentDescription = null)
            },
            title = {
                Text(text = stringResource(R.string.attention_))
            },
            text = {
                Text(text = state.value.loginResult.message ?: stringResource(CoreR.string.please_solve_form_input_issue))
            }
        )
    }

    if (initRecaptcha == false){
        AlertDialog(
            onDismissRequest = { initRecaptcha = null },
            confirmButton = {
                TextButton(
                    onClick = {
                        initRecaptcha = null
                    }
                ) {
                    Text(stringResource(R.string.try_again))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        (context as? Activity)?.finish()
                    }
                ) {
                    Text(stringResource(R.string.close_app))
                }
            },
            icon = {
                Icon(Icons.Default.Warning, contentDescription = null)
            },
            title = {
                Text(text = stringResource(R.string.attention_))
            },
            text = {
                Text(text = stringResource(R.string.recaptcha_has_failed_to_initiate_please_try_again_later))
            }
        )
    }

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors().copy(
            containerColor = BackgroundLight
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        )
    ) {

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            if(isLoading) CircularProgressIndicator()

            Column(
                modifier = Modifier
                    .padding(16.dp),
                horizontalAlignment = Alignment.Start
            ) {

                FormTextField(
                    value = state.value.username,
                    onValueChange = {
                        action(LoginUiAction.SetUsername(it))
                    },
                    label = stringResource(R.string.username_nik),
                    singleLine = true,
                    isLoading = isLoading,
                    inputData = state.value.usernameInput,
                    trailingIcon = {
                        Image(
                            painter = painterResource(id = R.drawable.baseline_people_24),
                            contentDescription = null
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                )

//                TextField(
//                    value = state.value.username,
//                    label = {
//                        Text(
//                            text = stringResource(R.string.username_nik)
//                        )
//                    },
//                    onValueChange = {
//                        action(LoginUiAction.SetUsername(it))
//                    },
//                    trailingIcon = {
//                        Image(
//                            painter = painterResource(id = R.drawable.baseline_people_24),
//                            contentDescription = null
//                        )
//                    },
//                    singleLine = true,
//                    modifier = Modifier
//                        .fillMaxWidth(),
//                    colors = TextFieldDefaults.colors().copy(
//                        focusedContainerColor = MaterialTheme.colorScheme.background,
//                        unfocusedContainerColor = MaterialTheme.colorScheme.background,
//                        disabledContainerColor = DisabledInputContainer
//                    ),
//                    enabled = !isLoading,
//                    isError = !state.value.usernameInput.isValid,
//                    supportingText = {
//                        ErrorValidationText(
//                            data = state.value.usernameInput,
//                            "Username",
//                        )
//                    }
//                )
                
                FormTextField(
                    value = state.value.password,
                    onValueChange = { action(LoginUiAction.SetPassword(it)) },
                    label = stringResource(R.string.password_no_kk),
                    singleLine = true,
                    isLoading = isLoading,
                    inputData = state.value.passwordInput,
                    trailingIcon = {
                        IconButton(onClick = {
                            if (state.value.passwordHidden) action(LoginUiAction.ShowPassword)
                            else action(LoginUiAction.HidePassword)
                        }) {
                            val iconVisibility =
                                if (state.value.passwordHidden) Icons.Filled.VisibilityOff
                                else Icons.Filled.Visibility
                            val description =
                                if (state.value.passwordHidden)
                                    stringResource(R.string.show_password)
                                else
                                    stringResource(R.string.hide_password)
                            Icon(
                                imageVector = iconVisibility, contentDescription = description
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth(),
                    visualTransformation = if (state.value.passwordHidden) PasswordVisualTransformation()
                    else VisualTransformation.None,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )

//                TextField(
//                    modifier = Modifier
//                        .fillMaxWidth(),
//                    colors = TextFieldDefaults.colors().copy(
//                        focusedContainerColor = MaterialTheme.colorScheme.background,
//                        unfocusedContainerColor = MaterialTheme.colorScheme.background,
//                        disabledContainerColor = DisabledInputContainer
//                    ),
//                    value = state.value.password,
//                    label = {
//                        Text(
//                            text = stringResource(R.string.password_no_kk)
//                        )
//                    },
//                    onValueChange = { action(LoginUiAction.SetPassword(it)) },
//                    singleLine = true,
//                    visualTransformation = if (state.value.passwordHidden) PasswordVisualTransformation()
//                    else VisualTransformation.None,
//                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
//                    trailingIcon = {
//                        IconButton(onClick = {
//                            if (state.value.passwordHidden) action(LoginUiAction.ShowPassword)
//                            else action(LoginUiAction.HidePassword)
//                        }) {
//                            val iconVisibility =
//                                if (state.value.passwordHidden) Icons.Filled.VisibilityOff
//                                else Icons.Filled.Visibility
//                            val description =
//                                if (state.value.passwordHidden)
//                                    stringResource(R.string.show_password)
//                                else
//                                    stringResource(R.string.hide_password)
//                            Icon(
//                                imageVector = iconVisibility, contentDescription = description
//                            )
//                        }
//                    },
//                    enabled = !isLoading,
//                    isError = !state.value.passwordInput.isValid,
//                    supportingText = {
//                        ErrorValidationText(
//                            state.value.passwordInput,
//                            "Password"
//                        )
//                    }
//                )

                AnimatedContent(
                    targetState = state.value.captcha,
                    transitionSpec = {
//                        (fadeIn() + slideInHorizontally(
//                            animationSpec = tween(400),
//                            initialOffsetX = { fullWidth -> fullWidth }
//                        )).togetherWith(
//                            fadeOut(
//                                animationSpec = tween(200)
//                            )
//                        )
                        fadeIn(
                            tween(1000)
                        ) togetherWith fadeOut(animationSpec = tween(1000))
                    },
                    label = "Captcha Button"
                ) { targetState ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(top = 10.dp, bottom = 10.dp)
                            .fillMaxWidth()
                            .border(
                                BorderStroke(
                                    width = 1.dp,
                                    color = if (!state.value.captchaInput.isValid)
                                        MaterialTheme.colorScheme.error else
                                        Color.Black.copy(
                                            alpha = 0.25f
                                        )
                                ),
                                RoundedCornerShape(5.dp)
                            )
                            .alpha(if (isLoading) 0.5f else 1F)
                            .clickable {
                                if (!isLoading) {
                                    validateCaptcha(action, scope, recaptchaClient, context)
                                }
                            }
                    ){
                        if (targetState is Resource.Loading){
                            // hide soft keyboard when loading
                            LocalFocusManager.current.clearFocus()

                            CircularProgressIndicator(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .width(32.dp),
                                color = MaterialTheme.colorScheme.primary,
                                trackColor = MaterialTheme.colorScheme.surfaceVariant,
                            )

                            Text(text = stringResource(R.string.please_wait))

                        }else{
                            Checkbox(checked = state.value.captcha is Resource.Success, onCheckedChange = {
                                if (!isLoading) {
                                    validateCaptcha(action, scope, recaptchaClient, context)
                                }
                            })

                            Text(text = stringResource(R.string.i_am_not_robot))
                        }
                    }

                }

                if(!state.value.captchaInput.isValid){
                    ErrorValidationText(
                        state.value.captchaInput,
                        labelName = "Captcha"
                    )
                }

                Button(
                    modifier = Modifier
                        .padding(top = 50.dp, bottom = 6.dp)
                        .fillMaxWidth()
                    ,
                    onClick = { action(LoginUiAction.Submit(userType)) },
                    colors = ButtonDefaults.buttonColors().copy(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    shape = RoundedCornerShape(5.dp),
                    enabled = state.value.enableLogin && !isLoading
                ) {
                    Text(
                        text = stringResource(R.string.login),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

private fun validateCaptcha(
    action: (LoginUiAction) -> Unit,
    scope: CoroutineScope,
    recaptchaClient: RecaptchaClient?,
    context: Context
) {

    action(LoginUiAction.SetCaptchaLoading)
    scope.launch {
        try {
            recaptchaClient?.let {
                it
                    .execute(RecaptchaAction.LOGIN)
                    .onSuccess {
                        action(
                            LoginUiAction.SetCaptchaResult(
                                isValid = true,
                                token = it
                            )
                        )
                    }
                    .onFailure {
                        LoginUiAction.SetCaptchaResult(
                            isValid = false,
                            error = it
                        )
                    }
            }
                ?: throw Throwable(message = "recaptchaClient is null")

        } catch (e: Exception) {
            Toast
                .makeText(
                    context,
                    e.localizedMessage,
                    Toast.LENGTH_LONG
                )
                .show()
        }
    }
}



@Preview(showBackground = true)
@Composable
fun LoginFormPreview() {
    KoinApplication(application = {
        modules(listOf(commonModule, authModule))
    }) {
        SILPUSITRONTheme {
            LoginForm(userType = UserType.APP) {}
        }
    }
}