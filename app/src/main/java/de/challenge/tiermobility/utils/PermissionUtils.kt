package de.challenge.tiermobility.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager

object PermissionUtils {
    /**
     * for real-life app fine location would be better,
     * but we okay with the coarse also
     */
    fun hasLocationPermission(appContext: Context) =
        PackageManager.PERMISSION_GRANTED == appContext.checkSelfPermission(
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
}