package com.android.esmond.myapplication

import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.util.Log
import java.io.IOException
import java.util.*

class GetAddressService : CustomIntentService() {

    private val TAG = "GetAddressService"

    override fun onHandleIntent(intent: Intent?) {
        if (checkIfExtraIsValid(intent)) return

        val coordinates = intent?.getStringExtra(LOCATION_EXTRA_KEY) ?: return
        val latitude = coordinates.split(",")[0].toDoubleOrNull() ?: return
        val longitude = coordinates.split(",")[1].toDoubleOrNull() ?: return

        val geocoder = Geocoder(this, Locale.getDefault())
        var addresses: List<Address> = emptyList()

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1)
        } catch (ioException: IOException) {
            Log.d(TAG, "Service not available")
        } catch (illegalArgumentException: IllegalArgumentException) {
            Log.d(TAG, "Invalid location data: $latitude, $longitude")
        }

        if (addresses.isEmpty()) {
            Log.d(TAG, "No addresses found")
        } else {

            // use this to get full address data
//            val address = addresses[0]
//            val addressFragments = with(address) {
//                (0..maxAddressLineIndex).map { getAddressLine(it) }
//            }
//            returnResult(RESULT_SUCCESS, addressFragments.joinToString(separator = "\n"), ADDRESS_EXTRA_KEY)

            // this is only City + Country names
            val shortAddress = addresses[0].locality + ", " + addresses[0].countryName
            returnResult(RESULT_SUCCESS, shortAddress, ADDRESS_EXTRA_KEY)
        }
    }

    private fun checkIfExtraIsValid(intent: Intent?): Boolean {
        receiver = intent?.getParcelableExtra(RECEIVER_EXTRA_KEY)

        if (intent == null || receiver == null) {
            return true
        }
        return false
    }
}