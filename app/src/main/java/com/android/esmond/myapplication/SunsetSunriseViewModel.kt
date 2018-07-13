package com.android.esmond.myapplication

import android.app.Application
import android.app.IntentService
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.os.ResultReceiver
import android.util.Log

const val HANDLER_THREAD_NAME = "Results Receiver Thread"

class SunsetSunriseViewModel(private val context: Application) : ViewModel() {
    private var resultReceiver: MyResultReceiver
    private var handler: Handler

    private lateinit var coordinatesExtra: String

    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location?) {
            setupCoordinates(location)
            startIntentService(GetAddressService(), coordinatesExtra, LOCATION_EXTRA_KEY)
            startIntentService(GetSunriseSunsetService(), coordinatesExtra, LOCATION_EXTRA_KEY)
        }

        override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {
        }

        override fun onProviderEnabled(p0: String?) {
        }

        override fun onProviderDisabled(p0: String?) {
        }
    }

    var location: MutableLiveData<String> = MutableLiveData()
    var sunrise: MutableLiveData<String> = MutableLiveData()
    var sunset: MutableLiveData<String> = MutableLiveData()

    init {
        val handlerThread = HandlerThread(HANDLER_THREAD_NAME)
        handlerThread.start()
        handler = Handler(handlerThread.looper)
        resultReceiver = MyResultReceiver(handler)
    }

    private fun setupCoordinates(location: Location?): Pair<String, String> {
        val latitude = location?.latitude.toString()
        val longitude = location?.longitude.toString()
        if (coordinatesExtra.isEmpty()) {
            coordinatesExtra = "$latitude,$longitude"
        }
        return Pair(latitude, longitude)
    }

    fun refreshData(coordinates: String = "") {
        coordinatesExtra = coordinates
        requestLocationUpdate()
    }

    private fun requestLocationUpdate() {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        try {
            locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, locationListener, handler.looper)
        } catch (exception: SecurityException) {
            exception.stackTrace
        }
    }

    private fun startIntentService(service: IntentService, extra: String = "", extraKey: String = "") {
        val intent = Intent(context, service::class.java).apply {
            if (extra.isNotEmpty()) {
                putExtra(extraKey, extra)
            }
            putExtra(RECEIVER_EXTRA_KEY, resultReceiver)
        }
        context.startService(intent)
    }

    private inner class MyResultReceiver internal constructor(handler: Handler)
        : ResultReceiver(handler) {

        override fun onReceiveResult(resultCode: Int, resultData: Bundle?) {
            if (resultCode == RESULT_SUCCESS) {
                getLocationData(resultData)
                getSunsetSunriseData(resultData)
            }
        }

        private fun getLocationData(resultData: Bundle?) {
            if (resultData?.getString(ADDRESS_EXTRA_KEY) != null) {
                location.postValue(resultData.getString(ADDRESS_EXTRA_KEY))
            }
        }

        private fun getSunsetSunriseData(resultData: Bundle?) {
            if (resultData?.getString(SUNRISE_SUNSET_EXTRA_KEY) != null) {
                sunrise.postValue(resultData.getString(SUNRISE_SUNSET_EXTRA_KEY).split(";")[0])
                sunset.postValue(resultData.getString(SUNRISE_SUNSET_EXTRA_KEY).split(";")[1])
            }
        }
    }
}