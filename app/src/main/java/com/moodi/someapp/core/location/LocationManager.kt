package com.moodi.someapp.core.location

import android.Manifest
import androidx.annotation.RequiresPermission
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.tasks.await

class LocationManager(private val locationModule: FusedLocationProviderClient) : LocationClient {

    @OptIn(ExperimentalCoroutinesApi::class)
    @RequiresPermission(anyOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    override suspend fun getCurrentLocation(): AppLocation {
        val cancellationTokenSource = CancellationTokenSource()
        val currentLocationTask =
            locationModule.getCurrentLocation(PRIORITY_HIGH_ACCURACY, cancellationTokenSource.token)
        val currentLocation = currentLocationTask.await(cancellationTokenSource)

        return AppLocation(
            latitude = currentLocation.latitude, longitude = currentLocation.longitude
        )
    }
}
