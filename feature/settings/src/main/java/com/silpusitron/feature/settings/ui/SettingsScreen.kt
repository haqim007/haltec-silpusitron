package com.silpusitron.feature.settings.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForwardIos
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.silpusitron.common.di.commonModule
import com.silpusitron.core.ui.R
import com.silpusitron.core.ui.component.LottieLoader
import com.silpusitron.core.ui.parts.SmallTopBar
import com.silpusitron.core.ui.theme.BackgroundLight
import com.silpusitron.core.ui.theme.DisabledInputContainer
import com.silpusitron.core.ui.theme.SILPUSITRONTheme
import com.silpusitron.core.ui.util.KoinPreviewWrapper
import com.silpusitron.data.mechanism.Resource
import com.silpusitron.shared.auth.di.authSharedModule
import com.silpusitron.feature.settings.di.settingsModule
import org.koin.androidx.compose.koinViewModel
import com.silpusitron.core.ui.R as CoreR

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = koinViewModel(),
    navigateToAccountProfileScreen: () -> Unit
){
    val action = {action: AccountUiAction -> viewModel.doAction(action)}
    val state by viewModel.state.collectAsState()

    var showLogoutConfirmationDialog by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = state.logoutResult) {
        if(state.logoutResult is Resource.Loading){
            if (showLogoutConfirmationDialog){
                showLogoutConfirmationDialog = false
            }
        }
    }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        SmallTopBar(
            shape = RoundedCornerShape(0.dp),
            modifier = Modifier.height(50.dp),
        ){
            Text(
                text = stringResource(R.string.settings),
                style = MaterialTheme.typography.titleLarge.copy(
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            )
        }
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .clickable {
                        navigateToAccountProfileScreen()
                    },
                border = BorderStroke(0.25.dp, MaterialTheme.colorScheme.secondary),
                colors = CardDefaults.cardColors().copy(
                    containerColor = BackgroundLight
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 2.5.dp
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                ) {
                    Icon(
                        modifier = Modifier.weight(1f),
                        imageVector = Icons.Outlined.Person,
                        contentDescription = null
                    )

                    Text(
                        modifier = Modifier.weight(6f),
                        text = stringResource(R.string.profile),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    )

                    Icon(
                        modifier = Modifier.weight(1f),
                        imageVector = Icons.AutoMirrored.Outlined.ArrowForwardIos,
                        contentDescription = null
                    )
                }
            }

            Card(
                modifier = modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .clickable {
                        if (state.logoutResult !is Resource.Loading) {
                            showLogoutConfirmationDialog = true
                        }
                    },
                border = BorderStroke(0.25.dp, MaterialTheme.colorScheme.secondary),
                colors = CardDefaults.cardColors().copy(
                    containerColor = BackgroundLight
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 2.5.dp
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                ) {
                    Icon(
                        modifier = Modifier.weight(1f),
                        imageVector = Icons.AutoMirrored.Outlined.Logout,
                        contentDescription = null
                    )

                    Text(
                        modifier = Modifier.weight(6f),
                        text = stringResource(R.string.logout),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    )

                    if (state.logoutResult is Resource.Loading){
                        CircularProgressIndicator(
                            modifier = Modifier.size(30.dp),
                            strokeWidth = 3.dp
                        )
                    }else{
                        Icon(
                            modifier = Modifier.weight(1f),
                            imageVector = Icons.AutoMirrored.Outlined.ArrowForwardIos,
                            contentDescription = null
                        )
                    }
                }
            }

            DialogLogout(
                showLogoutConfirmationDialog,
                onShow = {
                    showLogoutConfirmationDialog = it
                },
                onLogout = {
                    action(AccountUiAction.Logout)
                }
            )
        }
    }
}

@Composable
private fun DialogLogout(
    show: Boolean = false,
    onShow: (show: Boolean) -> Unit,
    onLogout: () -> Unit
){
    if (show){
        Dialog(
            onDismissRequest = {
                onShow(false)
            }
        ) {
            // Draw a rectangle shape with rounded corners inside the dialog
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(500.dp)
                    .padding(2.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors().copy(
                    containerColor = MaterialTheme.colorScheme.background
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    LottieLoader(
                        modifier = Modifier.size(200.dp),
                        jsonRaw = CoreR.raw.lottie_questioning
                    )

                    Text(
                        text = stringResource(id = R.string.are_you_sure_wanna_leave_app),
                        textAlign = TextAlign.Center
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 50.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        Button(
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors().copy(
                                contentColor = MaterialTheme.colorScheme.primary,
                                containerColor = Color.Transparent
                            ),
                            border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.primary),
                            onClick = onLogout
                        ) {
                            Text(text = stringResource(id = CoreR.string.ok))
                        }

                        Button(
                            modifier = Modifier.weight(1f),
                            onClick = { onShow(false) },
                            colors = ButtonDefaults.buttonColors().copy(
                                contentColor = MaterialTheme.colorScheme.onError,
                                containerColor = MaterialTheme.colorScheme.error
                            )
                        ) {
                            Text(text = stringResource(id = CoreR.string.cancel_2))
                        }
                    }


                }
            }
        }
    }
}

@Preview(apiLevel = 34)
@Composable
fun AccountScreen_Preview(){
    KoinPreviewWrapper(
        modules = listOf(commonModule, authSharedModule, settingsModule)
    ) {
        SILPUSITRONTheme {
            SettingsScreen(navigateToAccountProfileScreen = {})
        }
    }
}