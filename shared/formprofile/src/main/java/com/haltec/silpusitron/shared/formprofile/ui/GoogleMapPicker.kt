package com.haltec.silpusitron.shared.formprofile.ui

import android.annotation.SuppressLint
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ZoomOutMap
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.haltec.silpusitron.core.ui.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import java.util.Locale
import com.haltec.silpusitron.core.ui.R as CoreR


private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

@OptIn(ExperimentalPermissionsApi::class)
@SuppressLint("MissingPermission")
@Composable
fun GoogleMapPicker(
    modifier: Modifier = Modifier,
    latitude: Double?,
    longitude: Double?,
    isMapPermissionGranted: Boolean,
    onChange: (latitude: String, longitude: String) -> Unit
) {
    val context = LocalContext.current

    //Initialize it where you need it
    fusedLocationProviderClient = LocationServices
        .getFusedLocationProviderClient(context)

    var currentLocation by remember {
        mutableStateOf(
            LatLng(latitude ?: -8.100000, longitude ?: 112.150002)
        )
    }
    val cameraPosition = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(currentLocation, 17f)
    }
    // for dialog picker
    val dialogCameraPosition = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(currentLocation, 17f)
    }
    var uiSettings = remember {
        MapUiSettings()
    }
    val properties by remember {
        mutableStateOf(MapProperties())
    }

    var isMapLoaded by remember { mutableStateOf(false) }
    var currentLocationAddress: Address? by remember {
        mutableStateOf(null)
    }

    LaunchedEffect(key1 = Unit) {
        uiSettings = uiSettings.copy(
            scrollGesturesEnabled = false,
            rotationGesturesEnabled = false
        )
    }

    LaunchedEffect(key1 = currentLocation) {
        onChange(currentLocation.latitude.toString(), currentLocation.longitude.toString())
        getLocationDetails(
            currentLocation.latitude, currentLocation.longitude, context
        ).collectLatest {
            currentLocationAddress = it
        }
    }

    LaunchedEffect(key1 = isMapPermissionGranted) {
        if (isMapPermissionGranted){
            // Retrieve the current location asynchronously
            fusedLocationProviderClient.getCurrentLocation(
                Priority.PRIORITY_BALANCED_POWER_ACCURACY,
                CancellationTokenSource().token,
            ).addOnSuccessListener { location ->
                location?.let {
                    currentLocation = LatLng(it.latitude, it.longitude)
                }
            }.addOnFailureListener { exception ->
                Log.e("GoogleMapPicker", exception.message ?: "")
            }
        }
    }

    var openDialog by remember {
        mutableStateOf(false)
    }
    val markerState = rememberMarkerState(position = currentLocation)
    val dialogMarkerState = rememberMarkerState(position = currentLocation)

    LaunchedEffect(key1 = currentLocation) {
        markerState.position = currentLocation
        dialogMarkerState.position = currentLocation
        cameraPosition.position = CameraPosition.fromLatLngZoom(currentLocation, 17f)
        dialogCameraPosition.position = CameraPosition.fromLatLngZoom(currentLocation, 17f)
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {

        GoogleMap(
            modifier = Modifier
                .fillMaxSize(),
            onMapLoaded = { isMapLoaded = true },
            cameraPositionState = cameraPosition,
            uiSettings = uiSettings,
            properties = properties
        ) {
            Marker(
                state = markerState,
                draggable = true,
                title = stringResource(id = R.string.your_location),
                snippet = currentLocationAddress?.getAddressLine(0)
            )
        }

        IconButton(
            modifier = Modifier
                .align(Alignment.TopEnd),
            onClick = { openDialog = true }
        ) {
            Icon(
                imageVector = Icons.Filled.ZoomOutMap,
                stringResource(id = CoreR.string.choose_location)
            )
        }
    }

    if (openDialog){

        Dialog(
            onDismissRequest = { openDialog = false }
        ) {
            Box(
                Modifier
                    .size(500.dp),
            ) {
                GoogleMap(
                    modifier = Modifier
                        .padding(top = 50.dp)
                        .align(Alignment.Center),
                    onMapLoaded = { isMapLoaded = true },
                    cameraPositionState = dialogCameraPosition,
                    onMapClick = {
                        currentLocation = it
                    },
                    uiSettings = uiSettings.copy(
                        scrollGesturesEnabled = true,
                        rotationGesturesEnabled = true,
                        myLocationButtonEnabled = isMapPermissionGranted
                    ),
                    properties = properties.copy(
                        isMyLocationEnabled = isMapPermissionGranted
                    )
                ) {
                    Marker(
                        state = dialogMarkerState,
                        draggable = true,
                        title = stringResource(id = R.string.your_location),
                        snippet = currentLocationAddress?.getAddressLine(0)
                    )
                }

                IconButton(
                    modifier = Modifier.align(Alignment.TopEnd),
                    onClick = { openDialog = false }
                ) {
                    Icon(
                        tint = Color.White,
                        imageVector = Icons.Filled.Close,
                        contentDescription = stringResource(id = CoreR.string.close)
                    )
                }
            }
        }
    }

}

fun getLocationDetails(lat: Double, long: Double, context: Context): StateFlow<Address?> {
    val _markerAddressDetail = MutableStateFlow<Address?>(null)
    try {
        //Not a good practice to pass context in vm, instead inject this Geocoder
        val geocoder = Geocoder(context, Locale.getDefault())
        if (SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            geocoder.getFromLocation(//Pass LatLng and get address
                lat,
                long,
                1,
            ) { p0 ->
                _markerAddressDetail.update { p0[0] }
            }
        } else {
            val addresses = geocoder.getFromLocation(//This method is deprecated for >32
                lat,
                long,
                1,
            )
            _markerAddressDetail.value =
                if(!addresses.isNullOrEmpty()){//The address can be null or empty
                    addresses[0]
                }else{
                    null
                }
        }
    } catch (e: Exception) {
        _markerAddressDetail.value = null
    }

    return _markerAddressDetail.asStateFlow()
}