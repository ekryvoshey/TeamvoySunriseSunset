package com.android.esmond.myapplication

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ServerApi {
    @GET("json?")
    fun getSunsetSunrise(@Query("lat")latitude:String,
                         @Query("lng")longitude: String,
                         @Query("date")date: String,
                         @Query("formatted")formatted: String): Call<JsonObject>
}