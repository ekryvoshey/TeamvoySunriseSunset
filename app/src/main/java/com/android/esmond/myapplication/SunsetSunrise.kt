package com.android.esmond.myapplication

import com.google.gson.annotations.SerializedName

data class SunsetSunrise(@SerializedName("sunrise") var sunrise: String,
                         @SerializedName("sunset") var sunset: String,
                         @SerializedName("solar_noon") var solar_noon: String,
                         @SerializedName("day_length") var day_length: String,
                         @SerializedName("civil_twilight_begin") var civil_twilight_begin: String,
                         @SerializedName("civil_twilight_end") var civil_twilight_end: String,
                         @SerializedName("nautical_twilight_begin") var nautical_twilight_begin: String,
                         @SerializedName("nautical_twilight_end") var nautical_twilight_end: String,
                         @SerializedName("astronomical_twilight_begin") var astronomical_twilight_begin: String,
                         @SerializedName("astronomical_twilight_end") var astronomical_twilight_end: String) {
}