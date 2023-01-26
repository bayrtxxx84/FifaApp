package com.example.test.model.endpoints

import com.example.test.model.entities.api.oauth.PetsToken
import com.example.test.model.entities.api.oauth.PetsTokenBody
import com.example.test.model.entities.api.pets.PetsResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface PetsEndPoint {

    @Headers("Content-Type: application/json")
    @POST("oauth2/token")
    suspend fun getToken(
        @Body tokenPets: com.example.test.model.entities.api.oauth.PetsTokenBody
    ): Response<com.example.test.model.entities.api.oauth.PetsToken>


    @GET("animals")
    suspend fun getAllAPets(
        @Header("Authorization") authorization: String,
        @Query("page") page: Int,
        @Query("type") type: String
    ): Response<PetsResponse>
}