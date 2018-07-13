package com.android.esmond.myapplication

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.google.android.gms.location.places.ui.PlacePicker

class SunsetSunriseFragment : Fragment() {
    private lateinit var locationView: TextView
    private lateinit var sunriseView: TextView
    private lateinit var sunsetView: TextView
    private lateinit var selectNewPlace: Button

    private var coordinates: String? = null
    private lateinit var viewModel: SunsetSunriseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkForNewCoordinates()
    }

    private fun checkForNewCoordinates() {
        val bundle = this.arguments
        if (bundle != null) {
            val coordinatesExtra = bundle[FRAGMENT_EXTRA_KEY]
            if (coordinatesExtra != null) {
                coordinates = coordinatesExtra as String
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_sunset_sunrise, container, false)
        initViews(view)
        setupSelectPlaceButton()
        return view
    }

    private fun initViews(view: View) {
        locationView = view.findViewById(R.id.location_view_value)
        sunriseView = view.findViewById(R.id.sunrise_view_value)
        sunsetView = view.findViewById(R.id.sunset_view_value)
        selectNewPlace = view.findViewById(R.id.select_new_place_button)
    }

    private fun setupSelectPlaceButton() {
        selectNewPlace.setOnClickListener {
            startPlacePicker()
        }
    }

    private fun startPlacePicker() {
        val builder = PlacePicker.IntentBuilder()
        startActivityForResult(builder.build(activity), 1)
    }

    override fun onStart() {
        super.onStart()

        val (locationObserver, sunriseObserver, sunsetObserver) = setupObservers()
        setupViewModel(locationObserver, sunriseObserver, sunsetObserver)
        requestDataForViews()
    }

    private fun setupObservers(): Triple<Observer<String>, Observer<String>, Observer<String>> {
        val locationObserver = Observer<String> { location: String? ->
            locationView.text = location
        }
        val sunriseObserver = Observer<String> { sunrise: String? ->
            sunriseView.text = sunrise
        }
        val sunsetObserver = Observer<String> { sunset: String? ->
            sunsetView.text = sunset
        }
        return Triple(locationObserver, sunriseObserver, sunsetObserver)
    }

    private fun setupViewModel(locationObserver: Observer<String>, sunriseObserver: Observer<String>, sunsetObserver: Observer<String>) {
        viewModel = MyViewModelFactory(activity?.application!!).create(SunsetSunriseViewModel::class.java)
        viewModel.location.observe(this, locationObserver)
        viewModel.sunrise.observe(this, sunriseObserver)
        viewModel.sunset.observe(this, sunsetObserver)
    }

    private fun requestDataForViews() {
        if (coordinates != null) {
            viewModel.refreshData(coordinates!!)
        } else {
            viewModel.refreshData()
        }
    }
}