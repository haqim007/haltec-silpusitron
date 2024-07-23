package com.haltec.silpusitron.user.accountprofile.ui

import android.Manifest
import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.SaveAs
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.haltec.silpusitron.core.ui.component.SmallTopBar
import com.haltec.silpusitron.core.ui.theme.SILPUSITRONTheme
import com.haltec.silpusitron.core.ui.theme.gradientColors
import com.haltec.silpusitron.core.ui.util.KoinPreviewWrapper
import com.haltec.silpusitron.core.ui.util.PermissionRequester
import com.haltec.silpusitron.core.ui.util.isPermissionGranted
import com.haltec.silpusitron.shared.formprofile.di.formProfileModule
import com.haltec.silpusitron.shared.formprofile.ui.FormProfileScreen
import com.haltec.silpusitron.shared.formprofile.ui.FormProfileUiAction
import com.haltec.silpusitron.shared.formprofile.ui.FormProfileViewModel
import org.koin.androidx.compose.koinViewModel
import com.haltec.silpusitron.core.ui.R as CoreR

@Composable
fun AccountProfileScreen(
    formProfileViewModel: FormProfileViewModel = koinViewModel(),
    onNavigateBack: () -> Unit
){
    val formProfileState by formProfileViewModel.state.collectAsState()

    var hasLoadForm by remember {
        mutableStateOf(false)
    }

    val activity: Activity? = if (LocalInspectionMode.current) null else {
        (LocalContext.current as? Activity)
    }

    Scaffold(
        topBar = {
            Row(
                Modifier
                    .background(
                        brush = Brush.linearGradient(
                            gradientColors,
                            start = Offset(0f, Float.POSITIVE_INFINITY),
                            end = Offset(Float.POSITIVE_INFINITY, 1000f),
                        ),
                        shape = RoundedCornerShape(
                            bottomEnd = 20.dp, bottomStart = 20.dp
                        )
                    )
            ) {
                IconButton(
                    onClick = { onNavigateBack() },
                    colors = IconButtonDefaults.iconButtonColors()
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(id = CoreR.string.back),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
                SmallTopBar(
                    shape = RoundedCornerShape(0.dp),
                    modifier = Modifier.height(50.dp),
                ) {
                    Text(
                        text = stringResource(CoreR.string.profile),
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    )
                }
            }
        }
    ) { innerPadding ->

        PermissionRequester(
            permissions = listOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            onPermissionGranted = {},
            onPermissionDenied = onNavigateBack,
        )

        FormProfileScreen(
            modifier = Modifier.padding(innerPadding),
            hasLoadForm = hasLoadForm,
            setHasLoadForm = {
                hasLoadForm = true
            },
            isMapPermissionGranted = if (activity != null){
                isPermissionGranted(
                    activity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) && isPermissionGranted(
                    activity,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            } else false,
            state = formProfileState,
            action = { action -> formProfileViewModel.doAction(action) },
            withMap = true,
            onSuccessSubmit = onNavigateBack,
            additionalContent = {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    Button(
                        modifier = Modifier,
                        onClick = { formProfileViewModel.doAction(FormProfileUiAction.Submit) },
                        colors = ButtonDefaults.buttonColors().copy(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        shape = RoundedCornerShape(5.dp),
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = stringResource(id = CoreR.string.save),
                                fontWeight = FontWeight.Bold
                            )
                            Icon(
                                imageVector = Icons.Default.SaveAs,
                                contentDescription = null
                            )
                        }
                    }
                }
            }
        )
    }
}

@Preview
@Composable
fun AccountProfileScreen_Preview(){
    KoinPreviewWrapper(modules = listOf(formProfileModule)) {
        SILPUSITRONTheme {
            AccountProfileScreen(onNavigateBack = {})
        }
    }
}