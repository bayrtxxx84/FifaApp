package com.example.test.model.repositories

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create


class APIRepository {

    private fun getRetrofitBuilder(base: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(base)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun <T> buildTeamsService(service: Class<T>): T {
        val builder = getRetrofitBuilder("https://copa22.medeiro.tech/")
        return builder.create(service)
    }

    fun <T> buildPetsService(service: Class<T>): T {
        val builder = getRetrofitBuilder("https://api.petfinder.com/v2/")
        return builder.create(service)
    }
}