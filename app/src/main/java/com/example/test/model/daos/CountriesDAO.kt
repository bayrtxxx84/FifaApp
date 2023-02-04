package com.example.test.model.daos

import androidx.room.*
import com.example.test.model.entities.api.countries.Countries
import com.example.test.model.entities.database.CountriesDB

@Dao
interface CountriesDAO {

    @Query("select * from CountriesDB")
    fun getAllCountries() : List<CountriesDB>

    @Query("select * from CountriesDB where idApi = :idApi")
    fun getOneCountry(idApi : String) : CountriesDB

    @Query("select * from CountriesDB where id = :id")
    fun getOneCountryID(id : Int) : CountriesDB

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCountry(c : CountriesDB)

    @Delete
    fun deleteCountry(c : CountriesDB)
}