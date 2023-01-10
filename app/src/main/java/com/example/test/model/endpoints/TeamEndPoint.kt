package com.example.test.model.endpoints

import com.example.test.model.entites.api.Countries
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface TeamEndPoint {

    @GET("teams/{country}")
    fun getInfo(@Path("country") country: String): Response<Countries>

}