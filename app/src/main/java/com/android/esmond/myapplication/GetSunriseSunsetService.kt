package com.android.esmond.myapplication

import android.content.Intent
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonObject
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*

const val URL = "https://api.sunrise-sunset.org/"

class GetSunriseSunsetService : CustomIntentService() {
    private val TAG = "GetSunriseSunsetService"

    override fun onHandleIntent(intent: Intent?) {
        receiver = intent?.getParcelableExtra(RECEIVER_EXTRA_KEY) ?: return

        val coordinates = intent.getStringExtra(LOCATION_EXTRA_KEY) ?: return
        val latitude = coordinates.split(",")[0]
        val longitude = coordinates.split(",")[1]

        val retrofit = Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        val serverApi = retrofit.create(ServerApi::class.java)
        val result = serverApi.getSunsetSunrise(
                longitude,
                latitude,
                "today",
                "0")

        result.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>?, response: Response<JsonObject>?) {
                Log.d(TAG, "onResponse")
                val jsonObject = response?.body()!!["results"]
                val obj = Gson().fromJson(jsonObject, SunsetSunrise::class.java)

                val sunrise = DateTimeFormat.forPattern("HH:mm:ss").print(DateTime.parse(obj.sunrise))
                val sunset = DateTimeFormat.forPattern("HH:mm:ss").print(DateTime.parse(obj.sunset))

                val sunsetSunrise = "$sunrise;$sunset"
                returnResult(RESULT_SUCCESS, sunsetSunrise, SUNRISE_SUNSET_EXTRA_KEY)
            }

            override fun onFailure(call: Call<JsonObject>?, t: Throwable?) {
                Log.d(TAG, "onFailure: ${t?.message}")
            }
        })
    }
}