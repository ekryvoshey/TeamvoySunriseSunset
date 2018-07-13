package com.android.esmond.myapplication

import android.app.IntentService
import android.os.Bundle
import android.os.ResultReceiver
import android.provider.ContactsContract

const val RECEIVER_EXTRA_KEY = "${ContactsContract.Directory.PACKAGE_NAME}.RECEIVER_EXTRA_KEY"
const val RESULT_SUCCESS = 200
const val LOCATION_EXTRA_KEY = "${ContactsContract.Directory.PACKAGE_NAME}.LOCATION_EXTRA_KEY"
const val ADDRESS_EXTRA_KEY = "${ContactsContract.Directory.PACKAGE_NAME}.ADDRESS_EXTRA_KEY"
const val SUNRISE_SUNSET_EXTRA_KEY = "${ContactsContract.Directory.PACKAGE_NAME}.SUNRISE_SUNSET_EXTRA_KEY"

abstract class CustomIntentService: IntentService("") {

    internal var receiver: ResultReceiver? = null

    internal fun returnResult(resultCode: Int, message: String = "", key: String = "") {
        if (resultCode == RESULT_SUCCESS) {
            val bundle = Bundle().apply { putString(key, message) }
            receiver?.send(resultCode, bundle)
        }
    }
}