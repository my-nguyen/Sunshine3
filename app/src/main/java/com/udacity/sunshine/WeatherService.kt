package com.udacity.sunshine

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("daily")
    fun fetchWeather(
        @Query("q") zipcode: String,
        @Query("units") units: String,
        @Query("cnt") numDays: Int,
        @Query("appid") apiKey: String
    ): Call<Record>

    companion object {
        private const val BASE_URL = "https://api.openweathermap.org/data/2.5/forecast/"

        val instance: WeatherService by lazy {
            val retrofit =
                Retrofit.Builder().baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            retrofit.create(WeatherService::class.java)
        }
    }
}