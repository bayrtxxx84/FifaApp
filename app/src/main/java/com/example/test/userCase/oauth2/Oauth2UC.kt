package com.example.test.userCase.oauth2

import com.example.test.model.endpoints.PetsEndPoint
import com.example.test.model.entities.api.oauth.PetsTokenBody
import com.example.test.model.repositories.APIRepository

class Oauth2UC {

    suspend fun getTokenPets(): com.example.test.model.entities.api.oauth.PetsToken? {
        val c = try {
            val service = APIRepository().buildPetsService(PetsEndPoint::class.java)
            val response = service.getToken(PetsTokenBody())
            if (response.isSuccessful) {
                response.body()!!
            } else {
                throw Exception(response.message())
            }
        } catch (e: Exception) {
            throw Exception(e.message)
        }
        return c
    }
}