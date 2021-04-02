package hu.bme.aut.android.publictransporterapp.service

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Looper
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*

class Location(val context: Activity, val callback: (locationResult: LocationResult) -> Unit, val permissionId: Int) {
    private val locationProvider: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

    fun requestCurrentLocation() {
        val locationRequest =  LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 0
        locationRequest.fastestInterval = 0
        locationRequest.numUpdates = 1
        requestWithLocationRequest(locationRequest)
    }

    fun requestNecessaryPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) return true

        ActivityCompat.requestPermissions(context, arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ), permissionId)
        return false
    }

    private fun requestWithLocationRequest(locationRequest: LocationRequest) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) return

        locationProvider.requestLocationUpdates(
            locationRequest, object : LocationCallback(){
                override fun onLocationResult(locationResult: LocationResult) {
                    callback(locationResult)
                }
            }, Looper.myLooper()
        )
    }

    fun setupPeriodicLocationRequest() {
        val locationRequest =  LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 2_000
        locationRequest.fastestInterval = 2_000
        requestWithLocationRequest(locationRequest)
    }
}