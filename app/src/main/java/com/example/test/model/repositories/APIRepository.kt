package com.example.test.model.repositories

import retrofit2.Retrofit


class APIRepository {

    fun getRetrofitFIFA(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://copa22.medeiro.tech/")
            .build()
    }
}