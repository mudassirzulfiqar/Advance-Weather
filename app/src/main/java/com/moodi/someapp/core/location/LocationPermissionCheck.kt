package com.moodi.someapp.core.location

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

class LocationPermissionCheck(val context: Context) {

    fun checkCoursePermission() = ContextCompat.checkSelfPermission(
        context, android.Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
        context, android.Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED


}