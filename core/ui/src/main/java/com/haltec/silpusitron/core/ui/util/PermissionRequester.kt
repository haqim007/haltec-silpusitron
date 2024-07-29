package com.haltec.silpusitron.core.ui.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.DialogProperties
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.haltec.silpusitron.core.ui.R
import com.haltec.silpusitron.core.ui.R as CoreR


/**
 * Composable function to request location permissions and handle different scenarios.
 *
 * @param onPermissionGranted Callback to be executed when all requested permissions are granted.
 * @param onPermissionDenied Callback to be executed when any requested permission is denied.
 * @param onPermissionsRevoked Callback to be executed when previously granted permissions are revoked.
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionRequester(
    permissions: List<String>,
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit
) {
    if (LocalInspectionMode.current) return
    if (permissions.isEmpty()) return

    val context = LocalContext.current as Activity

    var shouldShowRationale by remember {
        mutableStateOf(
            permissions.any {
                ActivityCompat.shouldShowRequestPermissionRationale(
                    context,
                    it,
                )
            }
        )
    }

    var showDeniedDialog by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissionsResult ->
            val granted = permissionsResult.values.all { it }
            if (granted) {
                onPermissionGranted()
            } else {
//                 shouldShowRationale = permissionsResult.keys.any {
//                    ActivityCompat.shouldShowRequestPermissionRationale(
//                        context,
//                        it,
//                    )
//                }
//                if (!shouldShowRationale){
//                    showDeniedDialog = true
//                }
                shouldShowRationale = true
//                else{
//                    shouldShowRationale = permissionsResult.keys.any {
//                        ActivityCompat.shouldShowRequestPermissionRationale(
//                            context,
//                            it,
//                        )
//                    }
//                }
            }
        }
    )

    LaunchedEffect(key1 = Unit) {
        permissionLauncher.launch(
            permissions.toTypedArray()
        )
    }

    if (showDeniedDialog) {
        AlertDialog(
            properties = DialogProperties(
                dismissOnClickOutside = false,
                dismissOnBackPress = false
            ),
            onDismissRequest = { /* dismiss callback */ },
            title = { Text(stringResource(R.string.permission_needed)) },
            text = {
                Text(stringResource(R.string.app_need_location_and_folder_access))
            },
            confirmButton = {
                TextButton(onClick = {
                    showDeniedDialog = false
                    permissionLauncher.launch(permissions.toTypedArray())

                }) {
                    Text(stringResource(CoreR.string.allow))
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    onPermissionDenied()
                    showDeniedDialog = false
                }) {
                    Text(stringResource(id = CoreR.string.cancel))
                }
            }
        )
    }

    if (shouldShowRationale) {
        AlertDialog(
            properties = DialogProperties(
                dismissOnClickOutside = false,
                dismissOnBackPress = false
            ),
            onDismissRequest = {},
            title = { Text(stringResource(R.string.permission_needed)) },
            text = {
                Text(stringResource(R.string.permission_has_denied_before))
            },
            confirmButton = {
                TextButton(onClick = {
                    context.startActivity(
                        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            .apply {
                                data = Uri
                                    .fromParts("package", context.packageName, null)
                            }
                    )
                    shouldShowRationale = false
                }) {
                    Text(stringResource(R.string.open_settings))
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    shouldShowRationale = false
                    onPermissionDenied()
                }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }

    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                if (permissions.any { !isPermissionGranted(context, it) }) {
                    permissionLauncher.launch(permissions.toTypedArray())
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }


}

fun isPermissionGranted(context: Context, permission: String): Boolean {
    return ActivityCompat.checkSelfPermission(
        context,
        permission
    ) == PackageManager.PERMISSION_GRANTED
}