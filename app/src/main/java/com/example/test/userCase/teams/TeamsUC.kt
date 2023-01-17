package com.example.test.userCase.teams

import android.util.Log
import com.example.test.model.endpoints.TeamEndPoint
import com.example.test.model.entities.api.Countries
import com.example.test.model.repositories.APIRepository

class TeamsUC {

    suspend fun getInfoTeam(country: String): Countries? {
        var c: Countries? = null
        try {
            val service = APIRepository().getRetrofit()
            val response = service.create(TeamEndPoint::class.java).getInfo(country)
            if (response.isSuccessful) {
                c = response.body()!!
            } else {
                throw Exception("Fracaso conexion")
            }
        } catch (e: Exception) {
            Log.d("Error", e.message.toString())
        }
        return c
    }
}