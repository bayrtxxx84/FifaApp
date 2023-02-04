package com.example.test.userCase.teams

import android.util.Log
import com.example.test.model.endpoints.TeamEndPoint
import com.example.test.model.entities.api.countries.Countries
import com.example.test.model.entities.database.CountriesDB
import com.example.test.model.repositories.APIRepository
import com.example.test.utils.Test

class TeamsUC {


    suspend fun getOneCountry(item: Countries): Boolean {
        val conn = Test.getConn()
        val dao = conn.getCountriesDao()
        val itemdb = dao.getOneCountry(item.id)
        return if (itemdb == null) {
            false
        } else {
            true
        }
    }

    suspend fun getInfoTeam(country: String): Countries? {
        var c: Countries? = null
        try {
            val service = APIRepository().buildTeamsService(TeamEndPoint::class.java)
            val response = service.getInfo(country)
            if (response.isSuccessful) {
                c = response.body()!!
            }
        } catch (e: Exception) {
            Log.d("Error", e.message.toString())
        }
        return c
    }

    suspend fun saveCountry(item: Countries) {
        val conn = Test.getConn()
        val dao = conn.getCountriesDao()
        val itemdb = dao.getOneCountry(item.id)
        if (itemdb == null) {
            val country = CountriesDB(
                0,
                item.alternateName,
                item.country,
                item.id
            )
            dao.insertCountry(country)
        }

        Log.d(
            "UCE",
            conn.getCountriesDao().getAllCountries().toString()
        )
    }


    suspend fun deleteCountry(item: Countries) {
        val conn = Test.getConn()
        val dao = conn.getCountriesDao()
        val itemdb = dao.getOneCountry(item.id)
        if (itemdb != null) {
            dao.deleteCountry(itemdb)
        }

        Log.d(
            "UCE",
            conn.getCountriesDao().getAllCountries().toString()
        )
    }
}