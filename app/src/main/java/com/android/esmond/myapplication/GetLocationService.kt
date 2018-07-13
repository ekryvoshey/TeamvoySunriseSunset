package com.android.esmond.myapplication

import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log

class GetLocationService : CustomIntentService() {
    private val TAG = "GetLocationService"

    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(p0: Location?) {
            val latitude = p0?.latitude.toString()
            val longitude = p0?.longitude.toString()
            returnResult(RESULT_SUCCESS, "$latitude;$longitude", LOCATION_EXTRA_KEY)
            Log.d(TAG, "Longitude: $latitude, latitude: $longitude")
        }

        override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {
        }

        override fun onProviderEnabled(p0: String?) {
        }

        override fun onProviderDisabled(p0: String?) {
        }
    }

    override fun onHandleIntent(intent: Intent?) {
        receiver = intent?.getParcelableExtra(RECEIVER_EXTRA_KEY) ?: return

        val locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager?
        try {
            locationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0f, locationListener)
        } catch (exception: SecurityException) {
            Log.d(TAG, "Security exception on request location: ${exception.stackTrace}")
        }
    }
}