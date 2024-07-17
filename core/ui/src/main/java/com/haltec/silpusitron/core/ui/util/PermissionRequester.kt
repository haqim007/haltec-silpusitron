package com.haltec.silpusitron.core.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState


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
    onPermissionGranted: (permissionState: MultiplePermissionsState) -> Unit,
    onPermissionDenied: (permissionState: MultiplePermissionsState) -> Unit,
    onPermissionsRevoked: (permissionState: MultiplePermissionsState) -> Unit,
    onShouldShowRationale: @Composable ((relaunch: () -> Unit) -> Unit)? = null
) {
    // Initialize the state for managing multiple location permissions.
    val permissionState = rememberMultiplePermissionsState(permissions)

    // Use LaunchedEffect to handle permissions logic when the composition is launched.
    LaunchedEffect(key1 = permissionState) {
        // Filter permissions that need to be requested.
        val permissionsToRequest = permissionState.permissions.filter {
            !it.status.isGranted
        }
        // If there are permissions to request, launch the permission request.
        if (permissionsToRequest.isNotEmpty()) permissionState.launchMultiplePermissionRequest()
    }

    // Check if all previously granted permissions are revoked.
    val allPermissionsRevoked =
        permissionState.permissions.size == permissionState.revokedPermissions.size

    // Execute callbacks based on permission status.
    if (allPermissionsRevoked) {
        onPermissionsRevoked(permissionState)
    } else {
        if (permissionState.allPermissionsGranted) {
            onPermissionGranted(permissionState)
        }
        else if (permissionState.shouldShowRationale){
            onShouldShowRationale?.let { OnShouldShowRationaleView ->
                OnShouldShowRationaleView {
                    permissionState.launchMultiplePermissionRequest()
                }
            }
        }
        else {
            onPermissionDenied(permissionState)
        }
    }
}