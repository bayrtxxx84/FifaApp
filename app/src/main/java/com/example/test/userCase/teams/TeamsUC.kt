package com.example.test.userCase.teams

import android.util.Log
import com.example.test.model.endpoints.TeamEndPoint
import com.example.test.model.entites.api.Countries
import com.example.test.model.repositories.APIRepository

class TeamsUC {

    fun getInfoTeam(country: String): Countries? {

        try {
            val service = APIRepository().getRetrofitFIFA()
            val response = service.create(TeamEndPoint::class.java).getInfo(country)
            if (response.isSuccessful) {
                val c = response.body()!!
                c.country = c.country.uppercase()
            } else {
                throw Exception("La conexión ha fallado")
            }
        } catch (e: Exception) {
            Log.d("Error", e.message.toString())
        }
        return null
    }

}