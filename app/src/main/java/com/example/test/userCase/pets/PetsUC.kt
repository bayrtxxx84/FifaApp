package com.example.test.userCase.pets

import com.example.test.model.endpoints.PetsEndPoint
import com.example.test.model.entities.api.pets.Animal
import com.example.test.model.repositories.APIRepository

class PetsUC {

    suspend fun getAllPets(page: Int, type: String, auth: String): List<Animal>? {
        val service = APIRepository().buildPetsService(PetsEndPoint::class.java)
        val response = service.getAllAPets(auth, page, type)
        val resp = try {
            if (response.isSuccessful) {
                response.body()?.animals
            } else {
                listOf()
            }
        } catch (e: Exception) {
            null
        }
        return resp
    }
}