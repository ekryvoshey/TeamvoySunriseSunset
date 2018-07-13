package com.android.esmond.myapplication

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import com.google.android.gms.location.places.ui.PlacePicker

const val FRAGMENT_EXTRA_KEY = "FragmentExtraKey"

class SunsetSunriseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sunset_sunrise)
        supportFragmentManager.performTransaction { add(R.id.fragment_container, SunsetSunriseFragment()) }
    }

    private inline fun FragmentManager.performTransaction(function: FragmentTransaction.() -> Unit) {
        val fragmentTransaction = beginTransaction()
        fragmentTransaction.function()
        fragmentTransaction.commitAllowingStateLoss()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val bundle = setupBundle(data)
        val fragment = SunsetSunriseFragment()
        fragment.arguments = bundle
        supportFragmentManager.performTransaction { replace(R.id.fragment_container, fragment) }
    }

    private fun setupBundle(data: Intent?): Bundle {
        val place = PlacePicker.getPlace(this, data)
        val bundle = Bundle()
        val extraData = place.latLng.latitude.toString() + "," + place.latLng.longitude.toString()
        bundle.putString(FRAGMENT_EXTRA_KEY, extraData)
        return bundle
    }
}
